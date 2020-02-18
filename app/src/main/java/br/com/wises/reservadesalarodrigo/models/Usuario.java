package br.com.wises.reservadesalarodrigo.models;

public class Usuario {

	String nome, email, senha;
	int idOrganizacao;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public int getIdOrganizacao() {
		return idOrganizacao;
	}

	public void setIdOrganizacao(int idOrganizacao) {
		this.idOrganizacao = idOrganizacao;
	}
}
