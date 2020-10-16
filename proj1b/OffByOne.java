import java.lang.Math;
public class OffByOne implements CharacterComparator {
    @Override
    public boolean equalChars(char x, char y){
        int comparison = Math.abs(x - y);
        return comparison == 1;
    }
}
