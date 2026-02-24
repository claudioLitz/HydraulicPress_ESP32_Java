package br.senai.com.automacao.dashboard_iot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dados_sensores")
public class DadoSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float temperatura;
    private Float pressao;
    private Float corrente;
    private String status;

    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;

    public DadoSensor() {}

    @PrePersist
    public void prePersist() {
        this.dataLeitura = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Float getTemperatura() { return temperatura; }
    public void setTemperatura(Float temperatura) { this.temperatura = temperatura; }
    
    public Float getPressao() { return pressao; }
    public void setPressao(Float pressao) { this.pressao = pressao; }
    
    public Float getCorrente() { return corrente; }
    public void setCorrente(Float corrente) { this.corrente = corrente; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getDataLeitura() { return dataLeitura; }
    public void setDataLeitura(LocalDateTime dataLeitura) { this.dataLeitura = dataLeitura; }
}