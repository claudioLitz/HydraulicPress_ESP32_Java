package br.senai.com.automacao.dashboard_iot.repository;

import br.senai.com.automacao.dashboard_iot.model.DadoSensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DadoSensorRepository extends JpaRepository<DadoSensor, Long> {
}