/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        String[] datas = asciis.clone();
        int time = asciis[0].length();
        for (int i = time - 1; i >= 0; i--) {
            sortHelperLSD(datas,i);
//            for (int j = 0; j < asciis.length; j++) {
//                System.out.print(datas[j] + " ");
//            }
//            System.out.println();
        }
        return datas;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort

        // gather all the counts for each value
        int[] counts = new int[10];
        for (String i : asciis) {
            counts[i.charAt(index) - 48]++;
        }

        // when we're dealing with ints, we can just put each value
        // count number of times into the new array
        String[] sorted = new String[asciis.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1) {
                int cnt = 0;
                for (String num: asciis) {
                    if (num.charAt(index) - 48 == i && k < asciis.length) {
                        sorted[k] = num;
                        k++;
                        cnt++;
                    }
                }
                j += cnt - 1;
            }
        }

        for (int i = 0; i < asciis.length; i++) {
            asciis[i] = sorted[i];
        }
        // return the sorted array
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] sample = {"127","253","131", "999"};
        RadixSort.sort(sample);
    }



}
