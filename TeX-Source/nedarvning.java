class Dyr {
    String navn;

    Dyr(String navn) {
        this.navn = navn;
    }

    void lavLyd() {
        System.out.println("Dyr laver en lyd");
    }
}

class Hund extends Dyr {
    Hund(String navn) {
        super(navn); // Kalder constructor af superklassen
    }

    @Override // Kun i Java, unlad i Processing
    void lavLyd() {
        System.out.println("hunde lyde :)");
    }
}