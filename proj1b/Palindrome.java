public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        Deque<Character> words = new ArrayDeque<Character>();
        for(char i : word.toCharArray()){
            words.addLast(i);
        }
        return words;
    }
}
