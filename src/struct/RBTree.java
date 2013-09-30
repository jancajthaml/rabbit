package struct;

import java.util.Iterator;

/**
 * RB Tree implementation
 *
 * @author Jan Cajthaml
 */
public class RBTree<ELEMENT extends Comparable<ELEMENT>> implements Iterable<ELEMENT>
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

    private RBNode<ELEMENT> h		= new RBNode<ELEMENT>( null );
    private RBNode<ELEMENT> NIL		= null;
    private RBNode<ELEMENT> c		= null;
    private RBNode<ELEMENT> parent	= null;
    private RBNode<ELEMENT> gd		= null;
    private RBNode<ELEMENT> gr		= null;
    
	// >-------[ctor]---------------------------------------------------------------------------------------< //

    /**
     * Creates new empty RB Tree
     */
    public RBTree()
    { h.left = h.right = NIL; }
    
	// >-------[methods]---------------------------------------------------------------------------------------< //

    /**
     * 
     * @param x element
     */
    @Deprecated
    public void remove( ELEMENT x )
    { throw new UnsupportedOperationException( ); }
    
    /**
     * clears the Tree
     */
    public void makeEmpty( )
    { h.right = NIL; }
    
    /**
     * Check if tree is empty
     * 
     * @return true if tree is empty
     */
    public boolean isEmpty( )
    { return h.right == NIL; }
    
    /**
     * Inserts new node to tree
     *  
     * @param item element value
     */
    public void insert( ELEMENT item )
    {
        c		= parent
        		= gd
        		= h;
        NIL.e	= item;
        
        while( compare( item, c ) != 0 )
        {
            gr		= gd;
            gd		= parent;
            parent	= c;
            c		= compare( item, c ) < 0 ? c.left : c.right;
            
            if( !c.left.black && !c.right.black ) handleReorient( item );
        }
        
        if( c != NIL ) return;
        
        c = new RBNode<ELEMENT>( item, NIL, NIL );
        
        if( compare( item, parent ) < 0 )	parent.left		= c;
        else								parent.right	= c;
        handleReorient( item );
    }

    /**
     * Finds minimum value in tree
     * 
     * @return tree minimum
     */
    public ELEMENT findMin( )
    {
        if( isEmpty( ) ) return null;
        RBNode<ELEMENT> itr = h.right;
        while( itr.left != NIL )	itr = itr.left;
        return itr.e;
    }
    
    /**
     * Finds maximum value in tree
     * 
     * @return tre maximum
     */
    public ELEMENT findMax( )
    {
        if( isEmpty( ) ) return null;
        RBNode<ELEMENT> itr = h.right;
        while( itr.right != NIL ) itr = itr.right;
        return itr.e;
    }
    
    /**
     * Finds element value in tree
     * 
     * @param x element value
     * @return if element node exist return element value othervise return null
     */
    public ELEMENT find( ELEMENT x )
    {
        NIL.e = x;
        c = h.right;
        
        for(;;)
        {
            if( x.compareTo( c.e ) < 0 )		c = c.left;
            else if( x.compareTo( c.e ) > 0 )	c = c.right;
            else if( c != NIL )					return c.e;
            else								return null;
        }
    }
    
    /**
     * 
     * @param item
     */
    private void handleReorient( ELEMENT item )
    {
        c.black			= false;
        c.left.black	= true;
        c.right.black	= true;
        
        if( parent.black == false )
        {
            gd.black = false;
            if( ( compare( item, gd ) < 0 ) != ( compare( item, parent ) < 0 ) )
                parent	= rotate( item, gd );
            c			= rotate( item, gr );
            c.black		= true;
        }
        h.right.black = true;
    }

    /**
     * 
     * @param item
     * @param t
     * @return
     */
    private final int compare( ELEMENT item, RBNode<ELEMENT> t )
    { return ( t == h )?1:item.compareTo( t.e );	}
    
    /**
     * 
     * @param item
     * @param parent
     * @return
     */
    private RBNode<ELEMENT> rotate( ELEMENT item, RBNode<ELEMENT> parent )
    { return ( compare( item, parent ) < 0 )?(parent.left = compare( item, parent.left ) < 0 ?	rotateWithLeftChild( parent.left ):	rotateWithRightChild( parent.left )):(parent.right = compare( item, parent.right ) < 0 ? rotateWithLeftChild( parent.right ) :rotateWithRightChild( parent.right )); }
    
    /**
     * 
     * @param k2
     * @return
     */
    private static<ELEMENT extends Comparable<ELEMENT>> RBNode<ELEMENT> rotateWithLeftChild( RBNode<ELEMENT> k2 )
    {
        RBNode<ELEMENT> k1	= k2.left;
        k2.left				= k1.right;
        k1.right			= k2;
        return k1;
    }
    
    /**
     * 
     * @param k1
     * @return
     */
    private static<ELEMENT extends Comparable<ELEMENT>> RBNode<ELEMENT> rotateWithRightChild( RBNode<ELEMENT> k1 )
    {
        RBNode<ELEMENT> k2	= k1.right;
        k1.right			= k2.left;
        k2.left				= k1;
        return k2;
    }

    /**
     * 
     */
    @Deprecated
	public Iterator<ELEMENT> iterator()
	{
		return new Iterator<ELEMENT>()
		{
			public boolean hasNext()	{ return false;	}
			public ELEMENT next()		{ return null;	}
			public void remove()		{				}
		};
	}
	
	// >-------[nested]---------------------------------------------------------------------------------------< //

    private static class RBNode<ELEMENT extends Comparable<ELEMENT>>
    {
        RBNode( ELEMENT e )
        { this( e, null, null ); }
        
        RBNode( ELEMENT e, RBNode<ELEMENT> lt, RBNode<ELEMENT> rt )
        {
        	RBNode.this.e		= e;
        	RBNode.this.left	= lt;
        	RBNode.this.right	= rt;
        	RBNode.this.black	= true;
        }
        
        ELEMENT e				= null;
        RBNode<ELEMENT> left	= null;
        RBNode<ELEMENT> right	= null;
        boolean black			= false;
    }
    
	// >-------[post-processing]---------------------------------------------------------------------------------------< //

    {
        NIL			= new RBNode<ELEMENT>( null );
        NIL.left	= NIL.right = NIL;
    }
	
}