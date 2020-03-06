package br.com.wises.reservadesalarodrigo.models;

import java.util.Date;

public class Reserva {

	int id, id_sala, id_usuario;
	String nome_organizador, descricao;
	Date data_hora_inicio, data_hora_fim;

	public Reserva() {
	}

	public Reserva(int id, int id_sala, int id_usuario, String nome_organizador, String descricao, Date data_hora_inicio, Date data_hora_fim) {
		this.id = id;
		this.id_sala = id_sala;
		this.id_usuario = id_usuario;
		this.nome_organizador = nome_organizador;
		this.descricao = descricao;
		this.data_hora_inicio = data_hora_inicio;
		this.data_hora_fim = data_hora_fim;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_sala() {
		return id_sala;
	}

	public void setId_sala(int id_sala) {
		this.id_sala = id_sala;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getNome_organizador() {
		return nome_organizador;
	}

	public void setNome_organizador(String nome_organizador) {
		this.nome_organizador = nome_organizador;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData_hora_inicio() {
		return data_hora_inicio;
	}

	public void setData_hora_inicio(Date data_hora_inicio) {
		this.data_hora_inicio = data_hora_inicio;
	}

	public Date getData_hora_fim() {
		return data_hora_fim;
	}

	public void setData_hora_fim(Date data_hora_fim) {
		this.data_hora_fim = data_hora_fim;
	}
}
