package berkarya.kopas.id.quiz.util;

/**
 * Created by darshanz on 7/6/15.
 */
public class MengerjakanSoal {

    private String nomor,jawaban,ragu;
    private int layoutBgColor;

    public MengerjakanSoal(String nomor, String jawaban, String ragu) {
        this.nomor = nomor;
        this.jawaban = jawaban;
        this.ragu = ragu;
    }

    public void set_nomor(String nomor) {
        this.nomor = nomor;
    }
    public void set_jawab_soal(String jawaban) {
        this.jawaban = jawaban;
    }
    public void set_jawab_soal_ragu(String ragu) {
        this.ragu = ragu;
    }

    public String get_nomor() {
        return nomor;
    }
    public String get_jawab_soal() {
        return jawaban;
    }
    public String get_jawab_soal_ragu() {
        return ragu;
    }


    public int getLayoutBgColor() {
        return layoutBgColor;
    }

    public void setLayoutBgColor(int layoutBgColor) {
        this.layoutBgColor = layoutBgColor;
    }
}