public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        Deque<Character> words = new ArrayDeque<Character>();
        for(char i : word.toCharArray()){
            words.addLast(i);
        }
        return words;
    }

    public boolean isPalindrome(String word){
        Deque<Character> d = this.wordToDeque(word);
        for (int i = 0; i < d.size()/2; i++) {
            if (d.get(i) != d.get(d.size()-1-i)) {
                return false;
            }
        }
        return true;
    }
}
