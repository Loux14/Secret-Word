import java.util.*;

// Devoir 2 -- Lucas G

public class MotSecret{
    // version qui utilise les lettres majuscules de A à F
    public static final int tailleMax = 5;
    public static final char[] LETTRES = {'A', 'B', 'C', 'D', 'E', 'F'};
    private int taille;
    private char[] codeSecret;
    public  Random random = new Random();

    public MotSecret(int taille, CodeType t){
        this.taille = taille;
        codeSecret = new char[taille];
        if (t == CodeType.SANS_REP)
            genereAleatoireSansRep();
        else
            genereAleatoire();
    }
    
    public  void genereAleatoire(){
        for (int i = 0; i < taille; i++ ){
            char n;
            n = (char)(random.nextInt(5)+'A');
            codeSecret[i] = n ;
        }

    }

    public  void genereAleatoireSansRep(){
        int i = 0;
        while (i < taille) {
            char n;
            n = (char)(random.nextInt(5)+'A');
            boolean existe = false;
            for (int j = 0; j < i; j++){
                if (codeSecret[j] == n){
                    existe = true;
                    break;
                }
            }
            if (!existe){
                codeSecret[i] = n;
                i++;
            }
        }
    }
    
    public Paire compare(char[] essai){
        int bonnePlace=0;
        int mauvaisePlace = 0;

        //Creation des copies modifiables, c pour Code, e pour Essai
        char[] c = codeSecret.clone();
        char[] e = essai.clone();

        //Utilisation de '*' et '@' pour ne pas réutiliser des lettres
        for (int i = 0; i < taille; i++){
            if (e[i] == c[i]) {
                bonnePlace++;
                c[i] = '@';
                e[i] = '*';
            }
        }
        for (int i = 0; i < taille; i++){
            if (e[i] == '*') continue;

            for (int j = 0; j < taille; j++){
                if (e[i] == c[j]){
                    mauvaisePlace++;
                    c[j] = '@';
                    e[i] = '*';
                    break;
                }
            }
        }

        return new Paire(bonnePlace, mauvaisePlace);
    }


    public static boolean valideEssai(String essai, int n){
        boolean valide = true;
        if (essai.length() != n) valide = false;
        else {
            for (int i = 0; i < n; i++){
                if (essai.charAt(i) < 'A' || essai.charAt(i) > 'F'){
                    valide = false;
                    break;
                }
            }
        }
        return valide;
    }


    @Override
    public String toString() {
        return Arrays.toString(codeSecret);
    }


    public static void main(String[] args) {
        System.out.println("\nBienvenue au jeu de devinette du code secret!");
        System.out.println("Le code secret est un mot fait des lettres suivantes: " + Arrays.toString(LETTRES));
        System.out.println("Combien de lettres (entre 2 et 5) le code secret doit avoir ?");
        Scanner lecteur = new Scanner(System.in);
        int n = lecteur.nextInt();
        if (n < 2 || n > 5){
            System.out.println("nombre invalide");
            lecteur.close();
            return;
        }
        System.out.println("Est-ce que les lettres doivent être distinctes dans le code secret ? (oui/non)");
        String rep = lecteur.next();
        MotSecret code;
        if (rep.equals("non"))
            code = new MotSecret(n,CodeType.REP_PERMIS);
        else
        code = new MotSecret(n,CodeType.SANS_REP);
        //System.out.println(code);
        System.out.printf("L'ordinateur a généré le mot secret de %d lettres\n", n);
        System.out.println("À toi de le deviner!");
       
        String essai;
        String exemple="";
        for (int i = 0; i<n; i++) exemple += LETTRES[i]; // construction d'un exemple
        while (true ){
            while (true) {
                System.out.println("Entre ta conjecture (par exemple " + exemple +")");
                essai = lecteur.next().trim(); // on enlève les espaces
                if (valideEssai(essai, n)) break;
                System.out.printf("Mot invalide. Doit consiter en %d lettres parmis %s \n",n, Arrays.toString(LETTRES));
            }   
            Paire p = code.compare(essai.toCharArray());
            System.out.println("(bonne position, mauvaise position): "+ p);
            if (p.getFirst() == n) break;
        }
        System.out.println("Bravo, vous avez trouvé le mot secret!");
        lecteur.close();
    }


    enum CodeType {SANS_REP, REP_PERMIS};
     

    class Paire{

        private int first;
        private int second;

        public Paire(int first, int second){
            this.first = first;
            this.second = second;
        }

        int getFirst() {
            return first;
        }
        int getSecond() {
            return second;
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        } 
    }

}