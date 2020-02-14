package br.com.wises.reservadesalarodrigo.models;

public class Organizacao {

	String nome;
	int id_organizacao_pai;
	char tipo_dominio;

	public Organizacao() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId_organizacao_pai() {
		return id_organizacao_pai;
	}

	public void setId_organizacao_pai(int id_organizacao_pai) {
		this.id_organizacao_pai = id_organizacao_pai;
	}

	public char getTipo_dominio() {
		return tipo_dominio;
	}

	public void setTipo_dominio(char tipo_dominio) {
		this.tipo_dominio = tipo_dominio;
	}
}
