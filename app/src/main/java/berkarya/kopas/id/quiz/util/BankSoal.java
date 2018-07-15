package berkarya.kopas.id.quiz.util;

public class BankSoal{
    String no_soal, tanya_soal, tanya_soal_audio, jawab_a, jawab_b, jawab_c, jawab_d, jawab_e;

    public BankSoal(String no_soal, String tanya_soal, String tanya_soal_audio, String jawab_a, String jawab_b, String jawab_c, String jawab_d, String jawab_e) {
        this.no_soal = no_soal;
        this.tanya_soal = tanya_soal;
        this.tanya_soal_audio = tanya_soal_audio;
        this.jawab_a = jawab_a;
        this.jawab_b = jawab_b;
        this.jawab_c = jawab_c;
        this.jawab_d = jawab_d;
        this.jawab_e = jawab_e;
    }

    public String no_soal(){return no_soal;}
    public String tanya_soal(){return tanya_soal;}
    public String tanya_soal_audio(){return tanya_soal_audio;}
    public String jawab_a(){return jawab_a;}
    public String jawab_b(){return jawab_b;}
    public String jawab_c(){return jawab_c;}
    public String jawab_d(){return jawab_d;}
    public String jawab_e(){return jawab_e;}
}
