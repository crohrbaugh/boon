package org.boon.primitive;


import org.boon.Exceptions;
import org.boon.Universal;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class Byt {


    public static byte[] grow( byte[] array, final int size ) {
        Objects.requireNonNull ( array );

        byte[] newArray = new byte[array.length + size];
        System.arraycopy ( array, 0, newArray, 0, array.length );
        return newArray;
    }


    public static byte[] grow( byte[] array ) {
        Objects.requireNonNull ( array );

        byte[] newArray = new byte[array.length * 2];
        System.arraycopy ( array, 0, newArray, 0, array.length );
        return newArray;
    }


    public static byte[] shrink( byte[] array, int size ) {
        Objects.requireNonNull ( array );

        byte[] newArray = new byte[array.length - size];

        System.arraycopy ( array, 0, newArray, 0, array.length - size );
        return newArray;
    }


    public static byte[] compact( byte[] array ) {
        Objects.requireNonNull ( array );

        int nullCount = 0;
        for ( byte ch : array ) {

            if ( ch == '\0' ) {
                nullCount++;
            }
        }
        byte[] newArray = new byte[array.length - nullCount];

        int j = 0;
        for ( byte ch : array ) {

            if ( ch == '\0' ) {
                continue;
            }

            newArray[j] = ch;
            j++;
        }
        return newArray;
    }


    /**
     * Creates an array of bytes
     *
     * @param size size of the array you want to make
     * @return
     */
    public static byte[] arrayOfByte( final int size ) {
        return new byte[size];
    }

    /**
     * @param array
     * @return
     */
    @Universal
    public static byte[] array( final byte... array ) {
        Objects.requireNonNull ( array );
        return array;
    }

    /**
     * @param array
     * @return
     */
    @Universal
    public static byte[] bytes( final byte... array ) {
        Objects.requireNonNull ( array );
        return array;
    }

    /**
     * @param str
     * @return
     */
    @Universal
    public static byte[] bytes( String str ) {
        Objects.requireNonNull ( str );
        try {
            return str.getBytes ( "UTF-8" );
        } catch ( UnsupportedEncodingException ex ) {
            Exceptions.handle ( String.format ( "Unable to add \"%s\" ", str ), ex );
            return null;
        }
    }


    @Universal
    public static int len( byte[] array ) {
        return array.length;
    }


    @Universal
    public static byte idx( final byte[] array, final int index ) {
        final int i = calculateIndex ( array, index );

        return array[i];
    }


    @Universal
    public static void idx( final byte[] array, int index, byte value ) {
        final int i = calculateIndex ( array, index );

        array[i] = value;
    }


    @Universal
    public static byte[] slc( byte[] array, int startIndex, int endIndex ) {
        Objects.requireNonNull ( array );

        final int start = calculateIndex ( array, startIndex );
        final int end = calculateIndex ( array, endIndex );
        final int newLength = end - start;

        if ( newLength < 0 ) {
            throw new ArrayIndexOutOfBoundsException (
                    String.format ( "start index %d, end index %d, length %d",
                            startIndex, endIndex, array.length )
            );
        }

        byte[] newArray = new byte[newLength];
        System.arraycopy ( array, start, newArray, 0, newLength );
        return newArray;
    }

    @Universal
    public static byte[] slc( byte[] array, int startIndex ) {
        Objects.requireNonNull ( array );

        final int start = calculateIndex ( array, startIndex );
        final int newLength = array.length - start;

        if ( newLength < 0 ) {
            throw new ArrayIndexOutOfBoundsException (
                    String.format ( "start index %d, length %d",
                            startIndex, array.length )
            );
        }

        byte[] newArray = new byte[newLength];
        System.arraycopy ( array, start, newArray, 0, newLength );
        return newArray;
    }

    @Universal
    public static byte[] slcEnd( byte[] array, int endIndex ) {
        Objects.requireNonNull ( array );

        final int end = calculateIndex ( array, endIndex );
        final int newLength = end; // +    (endIndex < 0 ? 1 : 0);

        if ( newLength < 0 ) {
            throw new ArrayIndexOutOfBoundsException (
                    String.format ( "start index %d, length %d",
                            endIndex, array.length )
            );
        }

        byte[] newArray = new byte[newLength];
        System.arraycopy ( array, 0, newArray, 0, newLength );
        return newArray;
    }

    @Universal
    public static boolean in( int value, byte[] array ) {
        for ( int currentValue : array ) {
            if ( currentValue == value ) {
                return true;
            }
        }
        return false;
    }



    @Universal
    public static boolean inIntArray( byte value, int[] array ) {
        for ( int currentValue : array ) {
            if ( currentValue == value ) {
                return true;
            }
        }
        return false;
    }


    @Universal
    public static boolean in( int value, int offset, byte[] array ) {
        for ( int index = offset; index < array.length; index++ ) {
            int currentValue = array[index];
            if ( currentValue == value ) {
                return true;
            }
        }
        return false;
    }

    @Universal
    public static boolean in( int value, int offset, int end, byte[] array ) {
        for ( int index = offset; index < end; index++ ) {
            int currentValue = array[index];
            if ( currentValue == value ) {
                return true;
            }
        }
        return false;
    }


    @Universal
    public static byte[] copy( byte[] array ) {
        Objects.requireNonNull ( array );
        byte[] newArray = new byte[array.length];
        System.arraycopy ( array, 0, newArray, 0, array.length );
        return newArray;
    }

    @Universal
    public static byte[] copy( byte[] array, int offset, int length ) {
        Objects.requireNonNull ( array );
        byte[] newArray = new byte[length];
        System.arraycopy ( array, offset, newArray, 0, length );
        return newArray;
    }


    @Universal
    public static byte[] add( byte[] array, byte v ) {
        Objects.requireNonNull ( array );
        byte[] newArray = new byte[array.length + 1];
        System.arraycopy ( array, 0, newArray, 0, array.length );
        newArray[array.length] = v;
        return newArray;
    }

    @Universal
    public static byte[] add( byte[] array, byte[] array2 ) {
        Objects.requireNonNull ( array );
        byte[] newArray = new byte[array.length + array2.length];
        System.arraycopy ( array, 0, newArray, 0, array.length );
        System.arraycopy ( array2, 0, newArray, array.length, array2.length );
        return newArray;
    }


    @Universal
    public static byte[] insert( final byte[] array, final int idx, final byte v ) {
        Objects.requireNonNull ( array );

        if ( idx >= array.length ) {
            return add ( array, v );
        }

        final int index = calculateIndex ( array, idx );

        //Object newArray = Array.newInstance(array.getClass().getComponentType(), array.length+1);
        byte[] newArray = new byte[array.length + 1];

        if ( index != 0 ) {
            /* Copy up to the length in the array before the index. */
            /*                 src     sbegin  dst       dbegin   length of copy */
            System.arraycopy ( array, 0, newArray, 0, index );
        }


        boolean lastIndex = index == array.length - 1;
        int remainingIndex = array.length - index;

        if ( lastIndex ) {
            /* Copy the area after the insert. Make sure we don't write over the end. */
            /*                 src  sbegin   dst       dbegin     length of copy */
            System.arraycopy ( array, index, newArray, index + 1, remainingIndex );

        } else {
            /* Copy the area after the insert.  */
            /*                 src  sbegin   dst       dbegin     length of copy */
            System.arraycopy ( array, index, newArray, index + 1, remainingIndex );

        }

        newArray[index] = v;
        return newArray;
    }


    @Universal
    public static byte[] insert( final byte[] array, final int fromIndex, final byte[] values ) {
        Objects.requireNonNull ( array );

        if ( fromIndex >= array.length ) {
            return add ( array, values );
        }

        final int index = calculateIndex ( array, fromIndex );

        //Object newArray = Array.newInstance(array.getClass().getComponentType(), array.length+1);
        byte[] newArray = new byte[array.length + values.length];

        if ( index != 0 ) {
            /* Copy up to the length in the array before the index. */
            /*                 src     sbegin  dst       dbegin   length of copy */
            System.arraycopy ( array, 0, newArray, 0, index );
        }


        boolean lastIndex = index == array.length - 1;

        int toIndex = index + values.length;
        int remainingIndex = newArray.length - toIndex;

        if ( lastIndex ) {
            /* Copy the area after the insert. Make sure we don't write over the end. */
            /*                 src  sbegin   dst       dbegin     length of copy */
            System.arraycopy ( array, index, newArray, index + values.length, remainingIndex );

        } else {
            /* Copy the area after the insert.  */
            /*                 src  sbegin   dst       dbegin     length of copy */
            System.arraycopy ( array, index, newArray, index + values.length, remainingIndex );

        }

        for ( int i = index, j = 0; i < toIndex; i++, j++ ) {
            newArray[i] = values[j];
        }
        return newArray;
    }


    /* End universal methods. */
    private static int calculateIndex( byte[] array, int originalIndex ) {
        final int length = array.length;

        Objects.requireNonNull ( array, "array cannot be null" );


        int index = originalIndex;

        /* Adjust for reading from the right as in
        -1 reads the 4th element if the length is 5
         */
        if ( index < 0 ) {
            index = length + index;
        }

        /* Bounds check
            if it is still less than 0, then they
            have an negative index that is greater than length
         */
         /* Bounds check
            if it is still less than 0, then they
            have an negative index that is greater than length
         */
        if ( index < 0 ) {
            index = 0;
        }
        if ( index >= length ) {
            index = length - 1;
        }
        return index;
    }


    public static int idxInt( byte[] bytes, int off ) {
        return ( ( bytes[off + 3] & 0xFF ) ) +
                ( ( bytes[off + 2] & 0xFF ) << 8 ) +
                ( ( bytes[off + 1] & 0xFF ) << 16 ) +
                ( ( bytes[off] ) << 24 );
    }


    public static byte[] addInt( byte[] array, int v ) {
        Objects.requireNonNull ( array );

        byte[] arrayToHoldInt = new byte[4];
        intTo ( arrayToHoldInt, 0, v );
        return add ( array, arrayToHoldInt );

    }

    public static byte[] insertIntInto( byte[] array, int index, int v ) {
        Objects.requireNonNull ( array );

        byte[] arrayToHoldInt = new byte[4];
        intTo ( arrayToHoldInt, 0, v );
        return insert ( array, index, arrayToHoldInt );

    }


    public static void intTo( byte[] b, int off, int val ) {
        b[off + 3] = ( byte ) ( val );
        b[off + 2] = ( byte ) ( val >>> 8 );
        b[off + 1] = ( byte ) ( val >>> 16 );
        b[off] = ( byte ) ( val >>> 24 );
    }

    public static void longTo( byte[] b, int off, long val ) {
        b[off + 7] = ( byte ) ( val );
        b[off + 6] = ( byte ) ( val >>> 8 );
        b[off + 5] = ( byte ) ( val >>> 16 );
        b[off + 4] = ( byte ) ( val >>> 24 );
        b[off + 3] = ( byte ) ( val >>> 32 );
        b[off + 2] = ( byte ) ( val >>> 40 );
        b[off + 1] = ( byte ) ( val >>> 48 );
        b[off] = ( byte ) ( val >>> 56 );
    }

    public static byte[] addLong( byte[] array, long value ) {
        Objects.requireNonNull ( array );

        byte[] holder = new byte[8];
        longTo ( holder, 0, value );
        return add ( array, holder );

    }


    public static long idxUnsignedInt( byte[] bytes, int off ) {
        return ( ( bytes[off + 3] & 0xFFL ) ) +
                ( ( bytes[off + 2] & 0xFFL ) << 8L ) +
                ( ( bytes[off + 1] & 0xFFL ) << 16L ) +
                ( ( bytes[off] & 0xFFL ) << 24L );
    }

    public static long idxLong( byte[] b, int off ) {
        return ( ( b[off + 7] & 0xFFL ) ) +
                ( ( b[off + 6] & 0xFFL ) << 8 ) +
                ( ( b[off + 5] & 0xFFL ) << 16 ) +
                ( ( b[off + 4] & 0xFFL ) << 24 ) +
                ( ( b[off + 3] & 0xFFL ) << 32 ) +
                ( ( b[off + 2] & 0xFFL ) << 40 ) +
                ( ( b[off + 1] & 0xFFL ) << 48 ) +
                ( ( ( long ) b[off] ) << 56 );
    }


    public static short idxShort( byte[] b, int off ) {
        return ( short ) ( ( b[off + 1] & 0xFF ) +
                ( b[off] << 8 ) );
    }

    public static byte[] addShort( byte[] array, short value ) {
        Objects.requireNonNull ( array );

        byte[] holder = new byte[2];
        shortTo ( holder, 0, value );
        return add ( array, holder );

    }


    public static byte[] insertShortInto( byte[] array, int index, short value ) {
        Objects.requireNonNull ( array );

        byte[] holder = new byte[2];
        shortTo ( holder, 0, value );
        return insert ( array, index, holder );

    }


    public static void shortTo( byte[] b, int off, short val ) {
        b[off + 1] = ( byte ) ( val );
        b[off] = ( byte ) ( val >>> 8 );
    }


    public static char idxChar( byte[] b, int off ) {
        return ( char ) ( ( b[off + 1] & 0xFF ) +
                ( b[off] << 8 ) );
    }

    public static byte[] addChar( byte[] array, char value ) {
        Objects.requireNonNull ( array );

        byte[] holder = new byte[2];
        charTo ( holder, 0, value );
        return add ( array, holder );

    }

    public static byte[] insertCharInto( byte[] array, int index, char value ) {
        Objects.requireNonNull ( array );

        byte[] holder = new byte[2];
        charTo ( holder, 0, value );
        return insert ( array, index, holder );

    }


    public static void charTo( byte[] b, int off, char val ) {
        b[off + 1] = ( byte ) ( val );
        b[off] = ( byte ) ( val >>> 8 );
    }


    public static float idxFloat( byte[] array, int off ) {
        return Float.intBitsToFloat ( idxInt ( array, off ) );
    }

    public static byte[] addFloat( byte[] array, float value ) {
        Objects.requireNonNull ( array );

        byte[] holder = new byte[4];
        floatTo ( holder, 0, value );
        return add ( array, holder );

    }

    public static byte[] insertFloatInto( byte[] array, int index, float value ) {
        Objects.requireNonNull ( array );

        byte[] holder = new byte[4];
        floatTo ( holder, 0, value );
        return insert ( array, index, holder );

    }

    public static void floatTo( byte[] array, int off, float val ) {
        intTo ( array, off, Float.floatToIntBits ( val ) );
    }


    public static byte[] addDouble( byte[] array, double value ) {
        Objects.requireNonNull ( array );

        byte[] holder = new byte[4];
        doubleTo ( holder, 0, value );
        return add ( array, holder );

    }


    public static byte[] insertDoubleInto( byte[] array, int index, double value ) {
        Objects.requireNonNull ( array );

        byte[] holder = new byte[4];
        doubleTo ( holder, 0, value );
        return insert ( array, index, holder );

    }


    public static void doubleTo( byte[] b, int off, double val ) {
        longTo ( b, off, Double.doubleToLongBits ( val ) );
    }

    public static double idxDouble( byte[] b, int off ) {
        return Double.longBitsToDouble ( idxLong ( b, off ) );
    }

//
//
//    public static boolean booleanAt(byte[] b, int off) {
//        return b[off] != 0;
//    }
//
//
//    public static boolean booleanInBytePos1(int val) {
//        val = val & 0x01;
//        return val != 0;
//    }
//
//    public static boolean booleanInBytePos2(int val) {
//        val = val & 0x02;
//        return val != 0;
//    }
//
//
//    public static boolean booleanInBytePos3(int val) {
//        val = val & 0x04;
//        return val != 0;
//    }
//
//    public static boolean booleanInBytePos4(int val) {
//        val = val & 0x08;
//        return val != 0;
//    }
//
//    public static boolean booleanInBytePos1(byte[] b, int off) {
//        int val = b[off] & 0x01;
//        return val != 0;
//    }
//
//    public static boolean booleanInBytePos2(byte[] b, int off) {
//        int val = b[off] & 0x02;
//        return val != 0;
//    }
//
//
//    public static boolean booleanInBytePos3(byte[] b, int off) {
//        int val = b[off] & 0x04;
//        return val != 0;
//    }
//
//    public static boolean booleanInBytePos4(byte[] b, int off) {
//        int val = b[off] & 0x08;
//        return val != 0;
//    }
//
//    public static boolean booleanInBytePos5(byte[] b, int off) {
//        int val = b[off] & 0x10;
//        return val != 0;
//    }
//
//    public static boolean booleanInBytePos6(byte[] b, int off) {
//        int val = b[off] & 0x20;
//        return val != 0;
//    }
//
//    public static boolean booleanInBytePos7(byte[] b, int off) {
//        int val = b[off] & 0x40;
//        return val != 0;
//    }
//
//    public static boolean booleanInBytePos8(byte[] b, int off) {
//        int val = b[off] & 0x80;
//        return val != 0;
//    }
//
//
//    public static int byteAt(byte[] b, int off) {
//        return b[off];
//    }
//
//
//    public static int topNibbleAt(byte[] b, int off) {
//        return topNibbleAt (b[off] );
//    }
//
//    public static int bottomNibbleAt(byte[] b, int off) {
//        return bottomNibbleAt (b[off] );
//    }
//
//    public static int topNibbleAt(int val) {
//        return  (val & 0xF0);
//    }
//
//    public static int bottomNibbleAt(int val) {
//        return  (val & 0x0F);
//    }
//
//
//    public static char charAt1(byte[] b, int off) {
//        return (char) ((b[off + 1] & 0xFF) +
//                (b[off] << 8));
//    }
//


    public static void _idx( final byte[] array, int startIndex, byte[] input ) {
        try {

            System.arraycopy ( input, 0, array, startIndex, input.length );
        } catch ( Exception ex ) {
            Exceptions.handle ( String.format ( "array size %d, startIndex %d, input length %d",
                    array.length, startIndex, input.length ), ex );
        }
    }

    public static void _idx( final byte[] array, int startIndex, byte[] input, int length ) {
        try {

            System.arraycopy ( input, 0, array, startIndex, length );
        } catch ( Exception ex ) {
            Exceptions.handle ( String.format ( "array size %d, startIndex %d, input length %d",
                    array.length, startIndex, input.length ), ex );
        }
    }


    public static void _idx( final byte[] output, int ouputStartIndex, byte[] input, int inputOffset, int length ) {
        try {

            System.arraycopy ( input, inputOffset, output, ouputStartIndex, length );
        } catch ( Exception ex ) {
            Exceptions.handle ( String.format ( "array size %d, startIndex %d, input length %d",
                    output.length, ouputStartIndex, input.length ), ex );
        }
    }


    public static int idxUnsignedShort( byte[] buffer, int off ) {

        int ch1 = buffer[off] & 0xFF;
        int ch2 = buffer[off + 1] & 0xFF;

        return ( ch1 << 8 ) + ( ch2 << 0 );


    }

    public static short idxUnsignedByte( byte[] array, int location ) {
        return ( short ) ( array[location] & 0xFF );
    }


    public static void unsignedIntTo( byte[] b, int off, long val ) {
        b[off + 3] = ( byte ) ( val );
        b[off + 2] = ( byte ) ( val >>> 8 );
        b[off + 1] = ( byte ) ( val >>> 16 );
        b[off] = ( byte ) ( val >>> 24 );
    }

    public static void unsignedShortTo( byte[] buffer, int off, int value ) {

        buffer[off + 1] = ( byte ) ( value );
        buffer[off] = ( byte ) ( value >>> 8 );

    }

    public static void unsignedByteTo( byte[] buffer, int off, short value ) {
        buffer[off] = ( byte ) ( value );

    }

}
