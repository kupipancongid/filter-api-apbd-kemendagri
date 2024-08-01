package id.go.dinasesdmaceh.anggaran_json_filter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnggaranDTO {
    private String tahun;
    private String id_daerah;
    private String kode_wil_prop;
    private String kode_wil;
    private String nama_prop;
    private String nama_wil;
    private String id_urusan;
    private String kode_urusan;
    private String nama_urusan;
    private String id_bidang_urusan;
    private String kode_bidang_urusan;
    private String nama_bidang_urusan;
    private String id_program;
    private String kode_program;
    private String nama_program;
    private String kode_skpd;
    private String nama_skpd;
    private String kode_sub_skpd;
    private String nama_sub_skpd;
    private String id_giat;
    private String kode_giat;
    private String nama_giat;
    private String id_sub_giat;
    private String kode_sub_giat;
    private String nama_sub_giat;
    private String kode_akun;
    private String nama_akun;
    private Double rincian;
    private String indikator;
    private String target;
    private String satuan;
    private String spm;
    private String layanan_teks;
    private String is_stunting;
    private String is_miskin_ekstrem;
}
