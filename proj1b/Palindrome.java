public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> words = new ArrayDeque<Character>();
        for (char i : word.toCharArray()) {
            words.addLast(i);
        }
        return words;
    }

    public boolean isPalindrome(String word) {
        if (word.length() <= 1) {
            return true;
        } else if (word.toCharArray()[0] == word.toCharArray()[word.length()-1]) {
            return this.isPalindrome(word.substring(1,word.length()-1));
        }
        return false;
    }
}
