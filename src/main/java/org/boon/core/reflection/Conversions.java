package org.boon.core.reflection;

import org.boon.Dates;
import org.boon.Sets;
import org.boon.StringScanner;
import org.boon.core.Typ;
import org.boon.core.Value;
import org.boon.primitive.CharBuf;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;

import static org.boon.Exceptions.die;


public class Conversions {
    static Class<Conversions> types = Conversions.class;

    private static final Logger log = Logger.getLogger ( Conversions.class.getName ( ) );


    public static int toInt( Object obj ) {
        if ( obj.getClass ( ) == int.class ) {
            return int.class.cast ( obj );
        }
        try {
            if ( obj instanceof Number ) {
                return ( ( Number ) obj ).intValue ( );
            } else if ( obj instanceof Boolean || obj.getClass ( ) == Boolean.class ) {
                boolean value = toBoolean ( obj );
                return value ? 1 : 0;
            } else if ( obj instanceof CharSequence ) {
                try {
                    return Integer.parseInt ( ( ( CharSequence ) obj ).toString ( ) );
                } catch ( Exception ex ) {
                    char[] chars = toString ( obj ).toCharArray ( );
                    boolean found = false;
                    CharBuf builder = CharBuf.create ( chars.length );
                    for ( char c : chars ) {
                        if ( Character.isDigit ( c ) && !found ) {
                            found = true;
                            builder.add ( c );
                        } else if ( Character.isDigit ( c ) && found ) {
                            builder.add ( c );
                        } else if ( !Character.isDigit ( c ) && found ) {
                        }
                    }
                    try {
                        if ( builder.len ( ) > 0 ) {
                            return Integer.parseInt ( builder.toString ( ) );
                        }
                    } catch ( Exception ex2 ) {
                        log.warning ( String.format (
                                "unable to convert to byte and there was an exception %s",
                                ex2.getMessage ( ) ) );
                    }
                }
            } else {
            }
        } catch ( Exception ex1 ) {

            log.warning ( String.format (
                    "unable to convert to byte and there was an exception %s",
                    ex1.getMessage ( ) ) );

        }
        die ( String.format ( "Unable to convert %s to a int", obj.getClass ( ) ) );
        return -666; // die throws an exception

    }

    public static byte toByte( Object obj ) {
        if ( obj.getClass ( ) == byte.class ) {
            return byte.class.cast ( obj );
        } else if ( obj instanceof Number ) {
            return ( ( Number ) obj ).byteValue ( );
        } else {
            return ( byte ) toInt ( obj );
        }
    }

    public static short toShort( Object obj ) {

        if ( obj.getClass ( ) == short.class ) {
            return short.class.cast ( obj );
        } else if ( obj instanceof Number ) {
            return ( ( Number ) obj ).shortValue ( );
        } else {
            return ( short ) toInt ( obj );
        }
    }

    public static char toChar( Object obj ) {
        if ( obj.getClass ( ) == char.class ) {
            return char.class.cast ( obj );
        } else if ( obj instanceof Character ) {
            return ( ( Character ) obj ).charValue ( );
        } else if ( obj instanceof CharSequence ) {
            return obj.toString ( ).charAt ( 0 );
        } else if ( obj instanceof Number ) {
            return ( char ) toInt ( obj );
        } else if ( obj instanceof Boolean || obj.getClass ( ) == Boolean.class ) {
            boolean value = toBoolean ( obj );
            return value ? 'T' : 'F';
        } else if ( obj.getClass ( ).isPrimitive ( ) ) {
            return ( char ) toInt ( obj );
        } else {
            String str = toString ( obj );
            if ( str.length ( ) > 0 ) {
                return str.charAt ( 0 );
            } else {
                return '0';
            }
        }
    }

    public static long toLong( Object obj ) {

        if ( obj.getClass ( ) == long.class ) {
            return long.class.cast ( obj );
        }

        try {
            if ( obj instanceof Number ) {
                return ( ( Number ) obj ).longValue ( );
            } else if ( obj instanceof CharSequence ) {
                try {
                    return Long.parseLong ( ( ( CharSequence ) obj ).toString ( ) );
                } catch ( Exception ex ) {
                    char[] chars = toString ( obj ).toCharArray ( );

                    CharBuf builder = CharBuf.create ( chars.length );
                    boolean found = false;
                    for ( char c : chars ) {
                        if ( Character.isDigit ( c ) && !found ) {
                            found = true;
                            builder.add ( c );
                        } else if ( Character.isDigit ( c ) && found ) {
                            builder.add ( c );
                        } else if ( !Character.isDigit ( c ) && found ) {
                        }
                    }
                    try {
                        if ( builder.len ( ) > 0 ) {
                            return Long.parseLong ( builder.toString ( ) );
                        }
                    } catch ( Exception ex2 ) {
                        log.warning ( String.format (
                                "unable to convert to long and there was an exception %s",
                                ex2.getMessage ( ) ) );

                    }
                }
            } else {
                return toInt ( obj );
            }
        } catch ( Exception ex ) {
            log.warning ( String.format (
                    "unable to convert to long and there was an exception %s",
                    ex.getMessage ( ) ) );

        }

        die ( String.format ( "Unable to convert %s to a long", obj.getClass ( ) ) );
        return -666; // die throws an exception

    }

    final static Set<String> TRUE_SET = Sets.set ( "t", "true", "True", "y", "yes", "1", "aye",
            "T", "TRUE", "ok" );

    public static boolean toBoolean( Object obj ) {

        if ( obj.getClass ( ) == boolean.class ) {
            return boolean.class.cast ( obj );
        } else if ( obj instanceof Boolean ) {
            return ( ( Boolean ) obj ).booleanValue ( );
        } else if ( obj instanceof Number || obj.getClass ( ).isPrimitive ( ) ) {
            int value = toInt ( obj );
            return value != 0 ? true : false;
        } else if ( obj instanceof String || obj instanceof CharSequence
                || obj.getClass ( ) == char[].class ) {
            String str = Conversions.toString ( obj );
            if ( str.length ( ) == 0 ) {
                return false;
            } else {
                return Sets.in ( str, TRUE_SET );
            }
        } else if ( Reflection.isArray ( obj ) || obj instanceof Collection ) {
            return Reflection.len ( obj ) > 0;
        } else {
            return toBoolean ( Conversions.toString ( obj ) );
        }
    }

    public static double toDouble( Object obj ) {
        if ( obj.getClass ( ) == double.class ) {
            return ( Double ) obj;
        }

        try {
            if ( obj instanceof Double ) {
                return ( Double ) obj;
            } else if ( obj instanceof Number ) {
                return ( ( Number ) obj ).doubleValue ( );
            } else if ( obj instanceof CharSequence ) {
                try {
                    return Double.parseDouble ( ( ( CharSequence ) obj ).toString ( ) );
                } catch ( Exception ex ) {
//                    String svalue = str(obj);
//                    Matcher re = Regex.re(
//                            "[-+]?[0-9]+\\.?[0-9]+([eE][-+]?[0-9]+)?", svalue);
//                    if (re.find()) {
//                        svalue = re.group(0);
//                        return Double.parseDouble(svalue);
//                    }
                    die ( String.format ( "Unable to convert %s to a double", obj.getClass ( ) ) );
                    return Double.NaN;
                }
            } else {
            }
        } catch ( Exception ex ) {
            log.warning ( String.format (
                    "unable to convert to double and there was an exception %s",
                    ex.getMessage ( ) ) );
        }

        die ( String.format ( "Unable to convert %s to a double", obj.getClass ( ) ) );
        return -666d; // die throws an exception

    }

    public static float toFloat( Object obj ) {
        if ( obj.getClass ( ) == float.class ) {
            return ( Float ) obj;
        }

        try {
            if ( obj instanceof Float ) {
                return ( Float ) obj;
            } else if ( obj instanceof Number ) {
                return ( ( Number ) obj ).floatValue ( );
            } else if ( obj instanceof CharSequence ) {
                try {
                    return Float.parseFloat ( ( ( CharSequence ) obj ).toString ( ) );
                } catch ( Exception ex ) {
//                    String svalue = str(obj);
//                    Matcher re = Regex.re(
//                            "[-+]?[0-9]+\\.?[0-9]+([eE][-+]?[0-9]+)?", svalue);
//                    if (re.find()) {
//                        svalue = re.group(0);
//                        return Float.parseFloat(svalue);
//                    }
                    die ( String.format ( "Unable to convert %s to a float", obj.getClass ( ) ) );
                    return Float.NaN;
                }
            } else {
            }
        } catch ( Exception ex ) {

            log.warning ( String.format (
                    "unable to convert to float and there was an exception %s",
                    ex.getMessage ( ) ) );
        }

        die ( String.format ( "Unable to convert %s to a float", obj.getClass ( ) ) );
        return -666f; // die throws an exception

    }

    @SuppressWarnings("unchecked")
    public static <T> T coerce( Class<T> clz, Object value ) {

        if ( clz == Typ.string || clz == Typ.chars ) {
            return ( T ) value.toString ( );
        } else if ( clz == Typ.integer || clz == Typ.intgr ) {
            Integer i = toInt ( value );
            return ( T ) i;
        } else if ( clz == Typ.longWrapper || clz == Typ.lng ) {
            Long l = toLong ( value );
            return ( T ) l;
        } else if ( clz == Typ.doubleWrapper || clz == Typ.dbl ) {
            Double i = toDouble ( value );
            return ( T ) i;
        } else if ( clz == Typ.floatWrapper || clz == Typ.flt ) {
            Float i = toFloat ( value );
            return ( T ) i;
        } else if ( clz == Typ.stringArray ) {
            die ( "Need to fix this" );
            return null;
        } else if ( clz == Typ.bool || clz == Typ.bln ) {
            Boolean b = toBoolean ( value );
            return ( T ) b;
        }  else if ( Typ.isMap ( clz ) ) {
            if ( value instanceof Map ) {
                return ( T ) value;
            }
            return ( T ) toMap ( value );
        } else if ( clz.isArray ( ) ) {
            return ( T ) toPrimitiveArrayIfPossible ( clz, value );
        } else if ( Typ.isCollection ( clz ) ) {
            return toCollection ( clz, value );
        } else if ( clz != null && clz.getPackage ( ) != null && !clz.getPackage ( ).getName ( ).startsWith ( "java" )
                && Typ.isMap ( value.getClass ( ) ) && Typ.doesMapHaveKeyTypeString ( value ) ) {
            return ( T ) Reflection.fromMap ( ( Map<String, Object> ) value );
        } else if ( clz.isEnum () ) {
            return toEnum (  clz, value );

        } else {
            return ( T ) value;
        }
    }

    public static <T> T toEnum(Class<? extends Enum> cls ,  String value ) {
        return (T) Enum.valueOf ( cls,  value );

    }


    public static <T> T toEnum(Class<? extends Enum> cls ,  int value ) {
        Class<? extends Enum> clsEnum = ( Class<? extends Enum> ) cls;

        Enum[] enumConstants = clsEnum.getEnumConstants ();
        for (Enum e : enumConstants) {
            if (e.ordinal () == value) {
                return (T)e;
            }
        }

        die ("Can't convert ordinal value " + value + " into enum of type " + clsEnum);
        return (T)null;


    }


    public static <T> T toEnum(  Class<?> cls, Object value ) {

        if ( value instanceof CharSequence ) {
            return toEnum ( cls, value.toString () );
        } else if (value instanceof Number || value.getClass ().isPrimitive ()) {

            int i = toInt ( value );
            return toEnum ( cls, i );
        } else {
            die ("Can't convert  value " + value + " into enum of type " + cls);
            return (T)null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T toPrimitiveArrayIfPossible( Class<T> clz, Object value ) {
        if ( clz == Typ.intArray ) {
            return ( T ) iarray ( value );
        } else if ( clz == Typ.byteArray ) {
            return ( T ) barray ( value );
        } else if ( clz == Typ.charArray ) {
            return ( T ) carray ( value );
        } else if ( clz == Typ.shortArray ) {
            return ( T ) sarray ( value );
        } else if ( clz == Typ.longArray ) {
            return ( T ) larray ( value );
        } else if ( clz == Typ.floatArray ) {
            return ( T ) farray ( value );
        } else if ( clz == Typ.doubleArray ) {
            return ( T ) darray ( value );
        } else if ( value.getClass ( ) == clz ) {
            return ( T ) value;
        } else {
            int index = 0;
            Object newInstance = Array.newInstance ( clz.getComponentType ( ), Reflection.len ( value ) );
            Iterator<Object> iterator = iterator ( Typ.object, value );
            while ( iterator.hasNext ( ) ) {
                Reflection.idx ( newInstance, index, iterator.next ( ) );
                index++;
            }
            return ( T ) newInstance;
        }
    }


    public static double[] darray( Object value ) {
        //You could handleUnexpectedException shorts, bytes, longs and chars more efficiently
        if ( value.getClass ( ) == Typ.shortArray ) {
            return ( double[] ) value;
        }
        double[] values = new double[Reflection.len ( value )];
        int index = 0;
        Iterator<Object> iterator = iterator ( Object.class, value );
        while ( iterator.hasNext ( ) ) {
            values[index] = toFloat ( iterator.next ( ) );
            index++;
        }
        return values;
    }

    public static float[] farray( Object value ) {
        //You could handleUnexpectedException shorts, bytes, longs and chars more efficiently
        if ( value.getClass ( ) == Typ.floatArray ) {
            return ( float[] ) value;
        }
        float[] values = new float[Reflection.len ( value )];
        int index = 0;
        Iterator<Object> iterator = iterator ( Object.class, value );
        while ( iterator.hasNext ( ) ) {
            values[index] = toFloat ( iterator.next ( ) );
            index++;
        }
        return values;
    }

    public static long[] larray( Object value ) {
        //You could handleUnexpectedException shorts, bytes, longs and chars more efficiently
        if ( value.getClass ( ) == Typ.shortArray ) {
            return ( long[] ) value;
        }
        long[] values = new long[Reflection.len ( value )];
        int index = 0;
        Iterator<Object> iterator = iterator ( Object.class, value );
        while ( iterator.hasNext ( ) ) {
            values[index] = toLong ( iterator.next ( ) );
            index++;
        }
        return values;
    }

    public static short[] sarray( Object value ) {
        //You could handleUnexpectedException shorts, bytes, longs and chars more efficiently
        if ( value.getClass ( ) == Typ.shortArray ) {
            return ( short[] ) value;
        }
        short[] values = new short[Reflection.len ( value )];
        int index = 0;
        Iterator<Object> iterator = iterator ( Object.class, value );
        while ( iterator.hasNext ( ) ) {
            values[index] = toShort ( iterator.next ( ) );
            index++;
        }
        return values;
    }

    public static int[] iarray( Object value ) {
        //You could handleUnexpectedException shorts, bytes, longs and chars more efficiently
        if ( value.getClass ( ) == Typ.intArray ) {
            return ( int[] ) value;
        }
        int[] values = new int[Reflection.len ( value )];
        int index = 0;
        Iterator<Object> iterator = iterator ( Object.class, value );
        while ( iterator.hasNext ( ) ) {
            values[index] = toInt ( iterator.next ( ) );
            index++;
        }
        return values;
    }

    public static byte[] barray( Object value ) {
        //You could handleUnexpectedException shorts, ints, longs and chars more efficiently
        if ( value.getClass ( ) == Typ.byteArray ) {
            return ( byte[] ) value;
        }
        byte[] values = new byte[Reflection.len ( value )];
        int index = 0;
        Iterator<Object> iterator = iterator ( Object.class, value );
        while ( iterator.hasNext ( ) ) {
            values[index] = toByte ( iterator.next ( ) );
            index++;
        }
        return values;
    }

    public static char[] carray( Object value ) {
        //You could handleUnexpectedException shorts, ints, longs and chars more efficiently
        if ( value.getClass ( ) == Typ.charArray ) {
            return ( char[] ) value;
        }
        char[] values = new char[Reflection.len ( value )];
        int index = 0;
        Iterator<Object> iterator = iterator ( Typ.object, value );
        while ( iterator.hasNext ( ) ) {
            values[index] = toChar ( iterator.next ( ) );
            index++;
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    public static Iterator iterator( final Object value ) {
        return iterator ( null, value );
    }

    public static <T> Iterator<T> iterator( Class<T> class1, final Object value ) {


        if ( Reflection.isArray ( value ) ) {
            final int length = Reflection.arrayLength ( value );

            return new Iterator<T> ( ) {
                int i = 0;

                @Override
                public boolean hasNext( ) {
                    return i < length;
                }

                @Override
                public T next( ) {
                    T next = ( T ) Reflection.idx ( value, i );
                    i++;
                    return next;
                }

                @Override
                public void remove( ) {
                }
            };
        } else if ( Typ.isCollection ( value.getClass ( ) ) ) {
            return ( ( Collection<T> ) value ).iterator ( );
        } else if ( Typ.isMap ( value.getClass ( ) ) ) {
            Iterator<T> iterator = ( ( Map<String, T> ) value ).values ( ).iterator ( );
            return iterator;
        } else {
            return ( Iterator<T> ) Collections.singleton ( value ).iterator ( );
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T toCollection( Class<T> clz, Object value ) {
        if ( Typ.isList ( clz ) ) {
            return ( T ) toList ( value );
        } else if ( Typ.isSortedSet ( clz ) ) {
            return ( T ) toSortedSet ( value );
        } else if ( Typ.isSet ( clz ) ) {
            return ( T ) toSet ( value );
        } else {
            return ( T ) toList ( value );
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List toList( Object value ) {
        if ( value instanceof List ) {
            return ( List ) value;
        } else if ( value instanceof Collection ) {
            return new ArrayList ( ( Collection ) value );
        } else {
            ArrayList list = new ArrayList ( Reflection.len ( value ) );
            Iterator<Object> iterator = iterator ( Typ.object, value );
            while ( iterator.hasNext ( ) ) {
                list.add ( iterator.next ( ) );
            }
            return list;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Set toSet( Object value ) {
        if ( value instanceof Set ) {
            return ( Set ) value;
        } else if ( value instanceof Collection ) {
            return new HashSet ( ( Collection ) value );
        } else {
            HashSet set = new HashSet ( Reflection.len ( value ) );
            Iterator<Object> iterator = iterator ( Typ.object, value );
            while ( iterator.hasNext ( ) ) {
                set.add ( iterator.next ( ) );
            }
            return set;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static SortedSet toSortedSet( Object value ) {
        if ( value instanceof Set ) {
            return ( SortedSet ) value;
        } else if ( value instanceof Collection ) {
            return new TreeSet ( ( Collection ) value );
        } else {
            TreeSet set = new TreeSet ( );
            Iterator<Object> iterator = iterator ( Typ.object, value );
            while ( iterator.hasNext ( ) ) {
                set.add ( iterator.next ( ) );
            }
            return set;
        }
    }


    public static Map<String, Object> toMap( Object value ) {
        return Reflection.toMap ( value );
    }


    public static String toString( Object obj ) {
        return String.valueOf ( obj );
    }

    public static Number toWrapper( long l ) {
        if ( l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE ) {
            return toWrapper ( ( int ) l );
        } else {
            return Long.valueOf ( l );
        }
    }

    public static Number toWrapper( int i ) {
        if ( i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE ) {
            return Byte.valueOf ( ( byte ) i );
        } else if ( i >= Short.MIN_VALUE && i <= Short.MAX_VALUE ) {
            return Short.valueOf ( ( short ) i );
        } else {
            return Integer.valueOf ( i );
        }
    }

    public static Object wrapAsObject( boolean i ) {
        return Boolean.valueOf ( i );
    }


    public static Object wrapAsObject( byte i ) {
        return Byte.valueOf ( i );
    }

    public static Object wrapAsObject( short i ) {
        return Short.valueOf ( i );
    }

    public static Object wrapAsObject( int i ) {
        return Integer.valueOf ( i );
    }

    public static Object wrapAsObject( long i ) {
        return Long.valueOf ( i );
    }

    public static Object wrapAsObject( double i ) {
        return Double.valueOf ( i );
    }

    public static Object wrapAsObject( float i ) {
        return Float.valueOf ( i );
    }

    public static Object toArrayGuessType( Collection<?> value ) {
        Class<?> componentType = Reflection.getComponentType ( value );
        Object array = Array.newInstance ( componentType, value.size ( ) );
        @SuppressWarnings("unchecked")
        Iterator<Object> iterator = ( Iterator<Object> ) value.iterator ( );
        int index = 0;
        while ( iterator.hasNext ( ) ) {
            Reflection.idx ( array, index, iterator.next ( ) );
            index++;
        }
        return array;
    }


    public static Object toArray( Class<?> componentType, Collection<?> value ) {
        Object array = Array.newInstance ( componentType, value.size ( ) );
        @SuppressWarnings("unchecked")
        Iterator<Object> iterator = ( Iterator<Object> ) value.iterator ( );
        int index = 0;
        while ( iterator.hasNext ( ) ) {
            Reflection.idx ( array, index, iterator.next ( ) );
            index++;
        }
        return array;
    }

    public static <V> V[] array( Class<V> type, final Collection<V> array ) {
        return ( V[] ) Conversions.toArray ( type, array );
    }


    public static Date toDate( Calendar c ) {
        return c.getTime ( );

    }

    public static Date toDate( long value ) {
        return new Date ( value );
    }

    public static Date toDate( Long value ) {
        return new Date ( value );
    }

    public static Date toDate( String value ) {
        try {
            return toDateUS ( value );
        } catch ( Exception ex ) {
            try {
                return DateFormat.getDateInstance ( DateFormat.SHORT ).parse ( value );
            } catch ( ParseException e ) {
                die ( "Unable to parse date" );
                return null;
            }

        }
    }


    public static Date toDateUS( String string ) {

        String[] split = StringScanner.splitByChars ( string, new char[]{'.', '\\', '/', ':'} );

        if ( split.length == 3 ) {
            return Dates.getUSDate ( toInt ( split[0] ), toInt ( split[1] ), toInt ( split[2] ) );
        } else if ( split.length >= 6 ) {
            return Dates.getUSDate ( toInt ( split[0] ), toInt ( split[1] ), toInt ( split[2] ),
                    toInt ( split[3] ), toInt ( split[4] ), toInt ( split[5] )
            );
        } else {
            die ( String.format ( "Not able to parse %s into a US date", string ) );
            return null;
        }

    }

    public static Date toEuroDate( String string ) {

        String[] split = StringScanner.splitByChars ( string, new char[]{'.', '\\', '/', ':'} );

        if ( split.length == 3 ) {
            return Dates.getEuroDate ( toInt ( split[0] ), toInt ( split[1] ), toInt ( split[2] ) );
        } else if ( split.length >= 6 ) {
            return Dates.getEuroDate ( toInt ( split[0] ), toInt ( split[1] ), toInt ( split[2] ),
                    toInt ( split[3] ), toInt ( split[4] ), toInt ( split[5] )
            );
        } else {
            die ( String.format ( "Not able to parse %s into a Euro date", string ) );
            return null;
        }

    }


    public static Date toDate( Object value ) {
        if ( value instanceof Long ) {
            return toDate ( ( Long ) value );
        } else if ( value instanceof String ) {
            return toDate ( ( String ) value );
        } else {
            if ( value != null ) {
                return toDate ( value.toString ( ) );
            } else {
                die ( "Unable to convert set to date" );
                return null;
            }
        }
    }


    public interface Converter<TO, FROM> {
        TO convert( FROM from );
    }


    public static <TO, FROM> List<TO> map( Converter<TO, FROM> converter,
                                           List<FROM> fromList ) {

        ArrayList<TO> toList = new ArrayList<TO> ( fromList.size ( ) );

        for ( FROM from : fromList ) {
            toList.add ( converter.convert ( from ) );
        }

        return toList;
    }


    public static <TO, FROM> List<TO> mapFilterNulls( Converter<TO, FROM> converter,
                                                      List<FROM> fromList ) {

        ArrayList<TO> toList = new ArrayList<TO> ( fromList.size ( ) );

        for ( FROM from : fromList ) {
            TO converted = converter.convert ( from );
            if ( converted != null ) {
                toList.add ( converted );
            }
        }

        return toList;
    }


    public static Object unifyList( Object o ) {
        return unifyList ( o, null );
    }

    public static Object unifyList( Object o, List list ) {

        if ( list == null && !Reflection.isArray ( o ) && !( o instanceof Iterable ) ) {
            return o;
        }

        if ( list == null ) {
            list = new ArrayList ( 400 );
        }
        if ( Reflection.isArray ( o ) ) {
            int length = Reflection.len ( o );
            for ( int index = 0; index < length; index++ ) {
                unifyList ( Reflection.idx ( o, index ), list );
            }
        } else if ( o instanceof Iterable ) {
            Iterable i = ( ( Iterable ) o );
            for ( Object item : i ) {
                list = ( List ) unifyList ( item, list );
            }
        } else {
            list.add ( o );
        }

        return list;


    }


    public Number coerceNumber( Object inputArgument, Class<?> paraType ) {
        Number number = ( Number ) inputArgument;
        if ( paraType == int.class || paraType == Integer.class ) {
            return number.intValue ( );
        } else if ( paraType == double.class || paraType == Double.class ) {
            return number.doubleValue ( );
        } else if ( paraType == float.class || paraType == Float.class ) {
            return number.floatValue ( );
        } else if ( paraType == short.class || paraType == Short.class ) {
            return number.shortValue ( );
        } else if ( paraType == byte.class || paraType == Byte.class ) {
            return number.byteValue ( );
        }
        return null;
    }


}
