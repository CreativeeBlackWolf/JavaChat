
//+7 (xxx) xxx-xx-xx
public class Number {
    private String s;
    public Number(String s){
        this.s = s;
    }
    //метод, приводящий номер к стандарту
    public void standard() {
        if (s.contains("+") == false){
            s = s.replaceFirst("8", "+7");
        }
        s = s.replaceAll(" ", "");
        s = s.replaceAll("\\p{P}", "");
        s = s.replaceAll("-", "");
        ExceptionsForNumber forNumber = new ExceptionsForNumber();
        if (s.length() != 12){
            try{
                forNumber.NumberCheck();
            }

            catch (ExceptionsForNumber e){
            }
        }
        else {
            char[] z = s.toCharArray();
            s = "";
            for (int k = 0; k < z.length; k++) {
                s += z[k];
                if (k == 1 && z[2] != '(') {
                    s += " (";
                }
                if (k == 4 && z[6] != ')') {
                    s += ") ";
                }
                if (k == 7 && z[10] != '-') {
                    s += "-";
                }
                if (k == 9 && z[10] != '-') {
                    s += "-";
                }
            }
            System.out.println(s);
        }
    }
}

