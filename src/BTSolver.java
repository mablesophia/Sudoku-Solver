import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class BTSolver
{

	// =================================================================
	// Properties
	// =================================================================

	private ConstraintNetwork network;
	private SudokuBoard sudokuGrid;
	private Trail trail;

	private boolean hasSolution = false;

	public String varHeuristics;
	public String valHeuristics;
	public String cChecks;

	// =================================================================
	// Constructors
	// =================================================================

	public BTSolver ( SudokuBoard sboard, Trail trail, String val_sh, String var_sh, String cc )
	{
		this.network    = new ConstraintNetwork( sboard );
		this.sudokuGrid = sboard;
		this.trail      = trail;

		varHeuristics = var_sh;
		valHeuristics = val_sh;
		cChecks       = cc;
	}

	// =================================================================
	// Consistency Checks
	// =================================================================

	// Basic consistency check, no propagation done
	private boolean assignmentsCheck ( )
	{
		for ( Constraint c : network.getConstraints() )
			if ( ! c.isConsistent() )
				return false;

		return true;
	}

	/**
	 * Part 1 TODO: Implement the Forward Checking Heuristic
	 *
	 * This function will do both Constraint Propagation and check
	 * the consistency of the network
	 *
	 * (1) If a variable is assigned then eliminate that value from
	 *     the square's neighbors.
	 *
	 * Note: remember to trail.push variables before you assign them
	 * Return: true is assignment is consistent, false otherwise
	 */
    
    private boolean forwardChecking()
    {
        for( Variable v: network.getVariables() ) {
            trail.push(v);
            if( v.isAssigned() ) {
                for(Variable c: network.getNeighborsOfVariable(v)) {
                    c.removeValueFromDomain(v.getAssignment());
                    if(c.getDomain().isEmpty()) {
                        return false;
                    }
//                    for (Constraint x: network.getConstraintsContainingVariable(c)){
//                        if ( ! x.isConsistent()){
//                            return false;
//                        }
//                    }
                }
            }
        }
        return true;
    }

	/**
	 * Part 2 TODO: Implement both of Norvig's Heuristics
	 *
	 * This function will do both Constraint Propagation and check
	 * the consistency of the network
	 *
	 * (1) If a variable is assigned then eliminate that value from
	 *     the square's neighbors.
	 *
	 * (2) If a constraint has only one possible place for a value
	 *     then put the value there.
	 *
	 * Note: remember to trail.push variables before you assign them
	 * Return: true is assignment is consistent, false otherwise
	 */
	private boolean norvigCheck ( )
	{
        for( Variable v: network.getVariables() ) {
            trail.push(v);
            if( v.isAssigned() ) {
                for(Variable c: network.getNeighborsOfVariable(v)) {
                    c.removeValueFromDomain(v.getAssignment());
            
                    for(int val : v.getDomain()){
                        if(!c.getDomain().contains(val)){
                            v.assignValue(val);
                        }
                    }
                    for (Constraint x: network.getConstraintsContainingVariable(c)){
                        if ( ! x.isConsistent()){
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
	}

	/**
	 * Optional TODO: Implement your own advanced Constraint Propagation
	 *
	 * Completing the three tourn heuristic will automatically enter
	 * your program into a tournament.
	 */
	private boolean getTournCC ( )
	{
        for( Variable v: network.getVariables() ) {
            trail.push(v);
            if( v.isAssigned() ) {
                for(Variable c: network.getNeighborsOfVariable(v)) {
                    c.removeValueFromDomain(v.getAssignment());
                    
                    for(int val : v.getDomain()){
                        if(!c.getDomain().contains(val)){
                            v.assignValue(val);
                        }
                    }
                    for (Constraint x: network.getConstraintsContainingVariable(c)){
                        if ( ! x.isConsistent()){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
	}

	// =================================================================
	// Variable Selectors
	// =================================================================

	// Basic variable selector, returns first unassigned variable
	private Variable getfirstUnassignedVariable()
	{
		for ( Variable v : network.getVariables() )
			if ( ! v.isAssigned() )
				return v;

		// Everything is assigned
		return null;
	}

	/**
	 * Part 1 TODO: Implement the Minimum Remaining Value Heuristic
	 *
	 * Return: The unassigned variable with the smallest domain
	 */
	private Variable getMRV ()
	{
        int m = Integer.MAX_VALUE;
        
        for( Variable v : network.getVariables() ){
            if( ! v.isAssigned() )
                if( m > v.getDomain().size() )
                    m = v.getDomain().size();
        }
        if ( m != Integer.MAX_VALUE){
            for( Variable v : network.getVariables() ){
                if( m == v.getDomain().size() )
                    return v;
                
            }
        }
        return null;
	}

	/**
	 * Part 2 TODO: Implement the Degree Heuristic
	 *
	 * Return: The unassigned variable with the most unassigned neighbors
	 */
	private Variable getDegree ( )
	{
        ArrayList<int[]> count_list = new ArrayList<int[]>();
        int var_index = 0;
        int count_list_index = 0;
        for(Variable v : network.getVariables()){
            if( !v.isAssigned() ){
                int[] kv = new int[2];
                kv[0] = var_index;
                count_list.add(kv);
                for(Variable c : network.getNeighborsOfVariable(v))
                    if( !c.isAssigned() ){
                        count_list.get(count_list_index)[1] += 1;
                    }
                count_list_index++;
            }
            var_index++;
        }
        
        Comparator<int[]> sortComparator = new Comparator<int []>(){
            @Override
            public int compare(int[] a1, int[] a2){
                return a1[1] - a2[1];
            }
        };
        
        if (count_list.size() != 0){
            Collections.sort( count_list, sortComparator );
            return network.getVariables().get(count_list.get(0)[0]);
        }
        return null;
	}

	/**
	 * Part 2 TODO: Implement the Minimum Remaining Value Heuristic
	 *                with Degree Heuristic as a Tie Breaker
	 *
	 * Return: The unassigned variable with, first, the smallest domain
	 *         and, second, the most unassigned neighbors
	 */
    private int getDegree(Variable v){
        int degree = 0;
        for(Variable n : network.getNeighborsOfVariable(v)){
            if( !n.isAssigned() ){
                degree++;
            }
        }
        return degree;
    }
    private Variable MRVwithTieBreaker ( )
	{
        int m = Integer.MAX_VALUE;
        Variable c = null;
        
        for( Variable v: network.getVariables() ){
            if( ! v.isAssigned() ){
                if( v.getDomain().isEmpty() ){
                    return null;
                }
                if ( m != v.getDomain().size() ){
                    m = v.getDomain().size();
                    c = v;
                }
                else{
                    if(getDegree(v) > getDegree(c)){
                        continue;
                    }
                }
            }
        }
        return c;
	}

	/**
	 * Optional TODO: Implement your own advanced Variable Heuristic
	 *
	 * Completing the three tourn heuristic will automatically enter
	 * your program into a tournament.
	 */
	private Variable getTournVar ( )
	{
        int m = Integer.MAX_VALUE;
        Variable c = null;
        
        for( Variable v: network.getVariables() ){
            if( ! v.isAssigned() ){
                if( v.getDomain().isEmpty() ){
                    return null;
                }
                if ( m != v.getDomain().size() ){
                    m = v.getDomain().size();
                    c = v;
                }
                else{
                    if(getDegree(v) > getDegree(c)){
                        continue;
                    }
                }
            }
        }
        return c;
	}

	// =================================================================
	// Value Selectors
	// =================================================================

	// Default Value Ordering
	public List<Integer> getValuesInOrder ( Variable v )
	{
		List<Integer> values = v.getDomain().getValues();

		Comparator<Integer> valueComparator = new Comparator<Integer>(){

			@Override
			public int compare(Integer i1, Integer i2) {
				return i1.compareTo(i2);
			}
		};
		Collections.sort(values, valueComparator);
		return values;
	}

	/**
	 * Part 1 TODO: Implement the Least Constraining Value Heuristic
	 *
	 * The Least constraining value is the one that will knock the least
	 * values out of it's neighbors domain.
	 *
	 * Return: A list of v's domain sorted by the LCV heuristic
	 *         The LCV is first and the MCV is last
	 */
    
	public List<Integer> getValuesLCVOrder ( Variable v )
	{
        ArrayList<int[]> LCVList = new ArrayList<int[]>();
        List<Integer> ToReturnList = new ArrayList<Integer>();
        int i = 0;
        for( int val : v.getDomain() )
        {
            int[] kv = new int[2];
            kv[0] = val;
            //kv[1] = 0;
            LCVList.add(kv);
            
            for(Variable c : network.getNeighborsOfVariable(v))
            {
                if( !c.isAssigned() && c.getDomain().contains(val) )
                    LCVList.get(i)[1] ++;
            }
            i++;
        }
        
        Comparator<int[]> sortComparator = new Comparator<int []>(){
            @Override
            public int compare(int[] a1, int[] a2){
                return a1[1] - a2[1];
            }
        };
        Collections.sort( LCVList, sortComparator );
        
        for(int[] kv : LCVList)
        {
            ToReturnList.add(kv[0]);
        }
        return ToReturnList;
	}

	/**
	 * Optional TODO: Implement your own advanced Value Heuristic
	 *
	 * Completing the three tourn heuristic will automatically enter
	 * your program into a tournament.
	 */
	public List<Integer> getTournVal ( Variable v )
	{
        ArrayList<int[]> LCVList = new ArrayList<int[]>();
        List<Integer> ToReturnList = new ArrayList<Integer>();
        int i = 0;
        for( int val : v.getDomain() )
        {
            int[] kv = new int[2];
            kv[0] = val;
            //kv[1] = 0;
            LCVList.add(kv);
            
            for(Variable c : network.getNeighborsOfVariable(v))
            {
                if( !c.isAssigned() && c.getDomain().contains(val) )
                    LCVList.get(i)[1] ++;
            }
            i++;
        }
        
        Comparator<int[]> sortComparator = new Comparator<int []>(){
            @Override
            public int compare(int[] a1, int[] a2){
                return a1[1] - a2[1];
            }
        };
        Collections.sort( LCVList, sortComparator );
        
        for(int[] kv : LCVList)
        {
            ToReturnList.add(kv[0]);
        }
        return ToReturnList;
	}

	//==================================================================
	// Engine Functions
	//==================================================================

	public void solve ( )
	{
		if ( hasSolution )
			return;

		// Variable Selection
		Variable v = selectNextVariable();

		if ( v == null )
		{
			for ( Variable var : network.getVariables() )
			{
				// If all variables haven't been assigned
				if ( ! var.isAssigned() )
				{
					System.out.println( "Error" );
					return;
				}
			}

			// Success
			hasSolution = true;
			return;
		}

		// Attempt to assign a value
		for ( Integer i : getNextValues( v ) )
		{
			// Store place in trail and push variable's state on trail
			trail.placeTrailMarker();
			trail.push( v );

			// Assign the value
			v.assignValue( i );

			// Propagate constraints, check consistency, recurse
			if ( checkConsistency() )
				solve();

			// If this assignment succeeded, return
			if ( hasSolution )
				return;

			// Otherwise backtrack
			trail.undo();
		}
	}

	private boolean checkConsistency ( )
	{
		switch ( cChecks )
		{
			case "forwardChecking":
				return forwardChecking();

			case "norvigCheck":
				return norvigCheck();

			case "tournCC":
				return getTournCC();

			default:
				return assignmentsCheck();
		}
	}

	private Variable selectNextVariable ( )
	{
		switch ( varHeuristics )
		{
			case "MinimumRemainingValue":
				return getMRV();

			case "Degree":
				return getDegree();

			case "MRVwithTieBreaker":
				return MRVwithTieBreaker();

			case "tournVar":
				return getTournVar();

			default:
				return getfirstUnassignedVariable();
		}
	}

	public List<Integer> getNextValues ( Variable v )
	{
		switch ( valHeuristics )
		{
			case "LeastConstrainingValue":
				return getValuesLCVOrder( v );

			case "tournVal":
				return getTournVal( v );

			default:
				return getValuesInOrder( v );
		}
	}

	public boolean hasSolution ( )
	{
		return hasSolution;
	}

	public SudokuBoard getSolution ( )
	{
		return network.toSudokuBoard ( sudokuGrid.getP(), sudokuGrid.getQ() );
	}

	public ConstraintNetwork getNetwork ( )
	{
		return network;
	}
}
