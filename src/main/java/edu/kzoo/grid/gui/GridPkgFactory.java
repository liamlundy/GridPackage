// Class: GridPkgFactory
//
// Author: Alyce Brady
//
// This class is based on the College Board's MBSFactory class, as
// allowed by the GNU General Public License.  MBSFactory is a
// black-box class within the AP(r) CS Marine Biology Simulation
// case study (see
// http://www.collegeboard.com/student/testing/ap/compsci_a/case.html).
//
// License Information:
//   This class is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation.
//
//   This class is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.

package edu.kzoo.grid.gui;

import edu.kzoo.grid.ArrayListGrid;
import edu.kzoo.grid.BoundedGrid;
import edu.kzoo.grid.Direction;
import edu.kzoo.grid.Grid;
import edu.kzoo.grid.GridObject;
import edu.kzoo.grid.Location;

import java.lang.reflect.Constructor;
import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;

/**
 *  Grid GUI Support Package:<br>
 *
 *  The <code>GridPkgFactory</code> class provides a set of static methods 
 *  for constructing grids and the objects they contain, and for keeping
 *  track of what types of grids or grid objects are available to a
 *  specific application.
 *
 *  <p>
 *  The <code>GridPkgFactory</code> class is based on the
 *  College Board's <code>MBSFactory</code> class,
 *  as allowed by the GNU General Public License.
 *
 *  @author Julie Zelenski (author of MBSFactory)
 *  @author Alyce Brady
 *  @version 20 February 2016
 **/
public class GridPkgFactory
{
    // Static (class) variables maintained by the factory hold the sets
    // of known grid object, BoundedGrid, and UnboundedArrayListGrid classes.
    // The comparator used for the TreeSets will keep the sets sorted by
    // class name.
    private static Comparator<Class> classCmp = new Comparator<Class>() {
        public int compare(Class c1, Class c2) { 
            return c1.toString().compareTo(c2.toString());
        }};
    private static Set<Class> gridObjectClasses = new TreeSet<Class>(classCmp);
    private static Set<Class> boundedGridClasses = new TreeSet<Class>(classCmp);
    private static Set<Class> unboundedGridClasses =
                                new TreeSet<Class>(classCmp);

    private static Class<? extends Grid> defaultBoundedGridClass = BoundedGrid.class;
    private static Class<? extends Grid> defaultUnboundedGridClass = ArrayListGrid.Unbounded.class;

    // Class constants that define the parameter types for common Grid constructors
    private static final Class<?>[] TWO_ARG_TYPES = {Grid.class, Location.class};
    private static final Class<?>[] THREE_ARG_TYPES = {Grid.class, Location.class, Direction.class};
    private static final Class<?>[] FOUR_ARG_TYPES = {Grid.class, Location.class, Direction.class, Color.class};
    private static final Class<?>[] BOUNDED_ARGS = {int.class, int.class};
    private static final Class<?>[] UNBOUNDED_ARGS = null;


    /** Returns the class that serves as the default bounded grid class.
     *  The default class is <code>BoundedGrid</code> unless it has been set
     *  to something else by the <code>setDefaultBoundedGrid</code> method.
     */
    public static Class<? extends Grid> getDefaultBoundedGridClass()
    {
        return defaultBoundedGridClass;
    }

    /** Sets the class that serves as the default bounded grid class to
     *  the given parameter.
     *  (Precondition: <code>cls</code> must have a constructor that
     *  takes two <code>int</code> parameters representing the number of
     *  rows and number of columns in a new grid.)
     *    @param cls
     */
    public static void setDefaultBoundedGridClass(Class<? extends Grid> cls)
    {
        try
        {
            if (isValidGridClass(cls, BOUNDED_ARGS))
                defaultBoundedGridClass = cls;
        }
        catch (NoSuchMethodException e) 
        {
            throw new IllegalArgumentException(cls.getName() +
                " doesn't have the proper (int, int) constructor.");
        } 
    }

    /** Returns the class that serves as the default bounded grid class.
     *  The default class is <code>BoundedGrid</code> unless it has been set
     *  to something else by the <code>setDefaultBoundedGrid</code> method.
     */
    public static Class<? extends Grid> getDefaultUnboundedGridClass()
    {
        return defaultUnboundedGridClass;
    }

    /** Sets the class that serves as the default bounded grid class to
     *  the given parameter.
     *  (Precondition: <code>cls</code> must have a constructor that
     *  takes two <code>int</code> parameters representing the number of
     *  rows and number of columns in a new grid.)
     *    @param cls
     */
    public static void setDefaultUnboundedGridClass(Class<? extends Grid> cls)
    {
        try
        {
            if (isValidGridClass(cls, UNBOUNDED_ARGS))
                defaultUnboundedGridClass = cls;
        }
        catch (NoSuchMethodException e) 
        {
            throw new IllegalArgumentException(cls.getName() +
                " doesn't have the proper no-parameter constructor.");
        } 
    }

    /** Creates an object of the given class.
     *  @param   cls        	 class of new object
     *  @param   parameterTypes  parameter types expected by constructor
     *                           for class
     *  @param   parameters      actual parameters to pass to constructor
     *  @return  the newly created object
     *  @throws  RuntimeException   if an object of the specified class cannot
     *                              be constructed with the specified
     *                              parameters
     **/
    public static Object constructObject(Class<?> cls, Class<?>[] parameterTypes,
                                         Object[] parameters)                                        
    {
       Object newObject = null;        // the new object
       
        try 
        {
            // Construct an instance via class constructor.
            if ( parameterTypes == null || parameters == null )
            {
                // if no parameters; use default constructor
                newObject = cls.newInstance();
            }
            else
            {
               	// use the constructor that matches the parameterTypes
                Constructor<?> objCons = cls.getConstructor(parameterTypes);
                newObject = objCons.newInstance(parameters);
            }
            return newObject;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Cannot construct " + cls.getName() + 
                                       " object due to " + e);
        }
     }
     

    /** Creates an instance of the given grid object class using either a
     *  two-argument constructor that takes a grid and a location or a
     *  default constructor followed by a call to add the object to
     *  the given grid at the given location.
     *  @param   cls  	class of new grid object
     *  @param   grid 	grid in which this object will reside
     *  @param   loc  	location the object will occupy
     *  @return  the newly created grid object
     *  @throws  RuntimeException   if a grid object of the specified class
     *                              cannot be constructed with the specified
     *                              parameters
     **/
    public static Object constructGridObject(Class cls,
                                             Grid grid, Location loc)
    {
        Object[] parameters = {grid, loc};
        try
        {
            return constructObject(cls, TWO_ARG_TYPES, parameters);
        }
        catch (RuntimeException e)
        {
            GridObject o = (GridObject) constructObject(cls, null, null);
            grid.add(o, loc);
            return o;
        }
   }

    /** Creates an instance of the given grid object class using a
     *  three-argument constructor that takes a grid, a location,
     *  and a direction.
     *  @param   cls  	class of new grid object
     *  @param   grid 	grid in which this object will act
     *  @param   loc  	location the object will occupy
     *  @param   dir   	direction the object will face
     *  @return  the newly created grid object
     *  @throws  RuntimeException   if an object of the specified class cannot
     *                              be constructed with the specified
     *                              parameters
     **/
    public static Object constructGridObject(Class cls,
                                             Grid grid, Location loc,
                                             Direction dir)
    {
        Object[] parameters = {grid, loc, dir};
        return constructObject(cls, THREE_ARG_TYPES, parameters);
    }
    
    /** Creates an instance of the given grid object class using a
     *  four-argument constructor that takes a grid, a location,
     *  a direction, and a color.
     *  @param   cls  	class of new grid object
     *  @param   grid 	grid in which this object will act
     *  @param   loc  	location the object will occupy
     *  @param   dir   	direction the object will face
     *  @param   color 	color of the object
     *  @return  the newly created grid object
     *  @throws  RuntimeException   if an object of the specified class cannot
     *                              be constructed with the specified
     *                              parameters
     **/
    public static Object constructGridObject(Class cls,
                                             Grid grid, Location loc,
                                             Direction dir, Color color)
    {
        Object[] parameters = {grid, loc, dir, color};
        return constructObject(cls, FOUR_ARG_TYPES, parameters);
    }
    
    /** Creates an instance of a Grid using the default constructor
     *  of the given class. Used to create unbounded grids; no 
     *  dimensions are specified.
     *  @param   cls  	class of new grid
     *  @return  the newly created grid
     *  @throws  RuntimeException   if an object of the specified class cannot
     *                              be constructed with no parameters
     **/
    public static Grid constructGrid(Class cls)
    {
        return (Grid)constructObject(cls, UNBOUNDED_ARGS, null);
    }

    /** Creates an instance of a Grid using the 2-argument constructor
     *  of the given class. Used to create bounded grids;  
     *  dimensions must be specified.
     *  @param   cls  	class of new grid
     *  @return  the newly created grid
     *  @throws  RuntimeException   if an object of the specified class cannot
     *                              be constructed with the specified
     *                              number of rows and columns
     **/
    public static Grid constructGrid(Class cls, int numRows,
                                     int numCols)
    {
        Object[] parameters = {new Integer(numRows), new Integer(numCols)};
        return (Grid)constructObject(cls, BOUNDED_ARGS, parameters);
    }


    /** Returns the set of grid object classes known to the factory.  
     *  Classes are added to the factory via the
     *  <code>addGridObjClassNames</code> method.
     *  @return  the set of grid object classes
     **/
    public static Set<Class> gridObjectClasses() 
    { 
        return gridObjectClasses; 
    }
    
    /** Returns the set of bounded grid classes known to the factory.  
     *  Classes are added to the factory via the <code>addBoundedClassNames</code> method.
     *  @return  the set of bounded grid classes
     **/
    public static Set<Class> boundedGridClasses() 
    {
        return boundedGridClasses; 
    }
    
    /** Returns the set of unbounded grid classes known to the factory.  
     *  Classes are added to the factory via the <code>addUnboundedClassNames</code> method.
     *  @return  the set of unbounded grid classes
     **/
    public static Set<Class> unboundedGridClasses() 
    {
        return unboundedGridClasses; 
    }
    
    /** Helper to add new classes to the factory. Classes are specified by
     *  name. For each name, error-checking is done to ensure the named
     *  class exists, is accessible, and has the proper constructor. If all checks
     *  out, the class is added to the appropriate set maintained by the factory.
     *  On errors, a message is printed and the class is skipped.
     *  @param  classNames an array of class names
     *  @param  whichCategory  descriptive name for category: "bounded grid"
     *                         for any bounded grid class or "unbounded grid"
     *                         for any unbounded grid class; any other string
     *                         will be taken to indicate a grid object class
     **/
    protected static void addClassesToFactory(String[] classNames, 
                                              String whichCategory)
    {
        if (whichCategory.equals("bounded grid"))
        {
            addClassesToFactory(classNames, ClassCategory.A_BOUNDED_GRID);
        }
        else if (whichCategory.equals("unbounded grid"))
        {
            addClassesToFactory(classNames, ClassCategory.AN_UNBOUNDED_GRID);
        }
        else 
        {
            addClassesToFactory(classNames, ClassCategory.A_GRID_OBJECT);
        }
    }
   
    /** Helper to add new classes to the factory. Classes are specified by
     *  name. For each name, error-checking is done to ensure the named
     *  class exists, is accessible, and has the proper constructor. If all checks
     *  out, the class is added to the appropriate set maintained by the factory.
     *  On errors, a message is printed and the class is skipped.
     *  @param  classNames an array of class names
     *  @param  whichCategory  specifies whether the classes being added are
     *                         for bounded grids, unbounded grids, or grid
     *                         objects
     **/
    protected static void addClassesToFactory(String[] classNames, 
                                              ClassCategory whichCategory)
    {
        for (int i = 0; i < classNames.length; i++) 
        {
            String errStart = "Discarding " + whichCategory + " choice \"" + 
                              classNames[i] + "\" because ";
            try 
            {
                Class cls = Class.forName(classNames[i], true, 
                        Thread.currentThread().getContextClassLoader());
                if (whichCategory == ClassCategory.A_BOUNDED_GRID)
                {
                    if (isValidGridClass(cls, BOUNDED_ARGS))
                        boundedGridClasses.add(cls);
                }
                else if (whichCategory == ClassCategory.AN_UNBOUNDED_GRID)
                {
                    if (isValidGridClass(cls, UNBOUNDED_ARGS))
                        unboundedGridClasses.add(cls);
                }
                else 
                {
                    gridObjectClasses.add(cls);
                }
            }
            catch (ClassNotFoundException e)
            {
                System.err.println(errStart + "no class found with that name.");
            } 
            catch (ClassCastException e)
            {
                System.err.println(errStart + e.getMessage());
            }
            catch (NoSuchMethodException e) 
            {
                System.err.println(errStart + "it doesn't have the proper constructor.");
            } 
        }
    }

   
    /** Adds grid object classes to the set maintained by the factory.
     *  If the named class cannot be found or the class is not a proper
     *  GridObject object, an error message is printed and the class is not
     *  added to the set.
     *  @param  classNames an array of grid object class names
     **/
    public static void addGridObjClassNames(String[] classNames)
    {
        for (int i = 0; i < classNames.length; i++) 
        {
            String errStart = "Discarding " + ClassCategory.A_GRID_OBJECT +
                              " choice \"" + classNames[i] + "\" because ";
            try 
            {
                Class cls = Class.forName(classNames[i], true, 
                        Thread.currentThread().getContextClassLoader());
                // If we were to check for validity, it would be based
                // on what constructors?  (And how many?)
                    gridObjectClasses.add(cls);
            }
            catch (ClassNotFoundException e)
            {
                System.err.println(errStart + "no class found with that name.");
            } 
            catch (ClassCastException e)
            {
                System.err.println(errStart + e.getMessage());
            }
//             catch (NoSuchMethodException e) 
//             {
//                 System.err.println(errStart + "it doesn't have the proper constructor.");
//             } 
        }
    }
        
    /** Adds bounded grid classes to the set maintained by the factory. 
     *  If the named class cannot be found or the class is not a proper bounded
     *  grid, an error message is printed, and that class is not added to the set.
     *  @param  classNames an array of bounded grid class names
     **/
    public static void addBoundedGridClassNames(String[] classNames)
    {
        addClassesToFactory(classNames, ClassCategory.A_BOUNDED_GRID);
    }


    /** Adds unbounded grid classes to the set maintained by the factory. 
     *  If the named class cannot be found or the class is not a proper unbounded
     *  grid, an error message is printed, and that class is not added to the set.
     *  @param  classNames an array of unbounded grid class names
     **/
    public static void addUnboundedGridClassNames(String[] classNames)
    {
        addClassesToFactory(classNames, ClassCategory.AN_UNBOUNDED_GRID);
    }
    
    /** Verifies that a class has the required constructor and
      *  is properly assignable to Grid.  This is used to vet
      *  grid classes given to the factory before
      *  adding them to the list of known classes.
     **/
    public static boolean isValidGridClass(Class<?> cls,
                                           Class<?>[] ctorParameters)
    throws NoSuchMethodException
    {	
        return hasCorrectCtor(cls, Grid.class, ctorParameters);
    }
    
    /** Verifies that a class has the required constructor and
      *  is properly assignable to GridObject.  This is used to vet
      *  grid object classes given to the factory before
      *  adding them to the list of known classes.
     **/
    public static boolean isValidGridObjectClass(Class<?> cls,
                                                 Class<?>[] ctorParameters)
    throws NoSuchMethodException
    {	
        return hasCorrectCtor(cls, GridObject.class, ctorParameters);
    }
    
    /** Verifies that a class has the required constructor and
      *  is properly assignable. This is used to vet classes
      *  given to the factory before adding them to the list of known classes.
     **/
    protected static boolean hasCorrectCtor(Class<?> cls, Class<?> requiredCls,
                                            Class<?>[] ctorParameters)
    throws NoSuchMethodException
    {	
            if (!requiredCls.isAssignableFrom(cls)) 
                throw new ClassCastException("not compatible with " + requiredCls + ".");
            Constructor<?> ctor = cls.getConstructor(ctorParameters);
            return true;
    }
    
    
    /** Reports wehther a given grid object class has an accessible
     *  four-arg constructor that takes Grid, Location, Direction,
     *  and Color.
     **/
    public static boolean hasFourArgCtor(Class<?> cls)
    {
        try 
        {
            return isValidGridObjectClass(cls, FOUR_ARG_TYPES);
        }
        catch (NoSuchMethodException e) {}
        return false;
    }

    protected static class ClassCategory
    {
        // Public constants specifying categories of objects made by this factory.

        /** Indicates a class representing a bounded grid with a two-parameter
         *  constructor specifying the number of rows and number of columns
         *  in the grid.
         **/
        protected static ClassCategory A_BOUNDED_GRID = new ClassCategory(0);

        /** Indicates a class representing an unbounded grid with a default
         *  (no-parameter) constructor.
         **/
        protected static ClassCategory AN_UNBOUNDED_GRID = new ClassCategory(1);

        /** Indicates a class representing a bounded grid. **/
        protected static ClassCategory A_GRID_OBJECT = new ClassCategory(2);

        // The value that makes each category unique.
        private int value;

        /** Constructs a new ClassCategory object.
         * 	  @param categoryCode  an integer value specifying a class category
         */
        protected ClassCategory(int categoryCode)
        {
            value = categoryCode;
        }
    }

}
