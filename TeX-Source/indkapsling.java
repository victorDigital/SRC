public class Person {
    private String CPR;
    
    public Person(String CPR) {
        this.CPR = CPR;
    }

    public String getCPR() {
        return CPR;
    }

    public void setCPR(String newCPR) {
        CPR = newCPR;
    }
}