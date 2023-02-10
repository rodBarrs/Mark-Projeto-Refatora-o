/**
 * @author Felipe Marques, João Paulo, Gabriel Ramos, Rafael Henrique
 * 
 * Responsável por indicar quais são as chaves de uso do banco de dados
 */

package com.mycompany.newmark;

public class Chaves_Banco {

	private String PALAVRACHAVE = null;
	private String COMPLEMENTO = null;
	private String ETIQUETA = null;
	private String TIPO = null;
	private String SUBNUCLEO= null;
	private String PRIORIDADE = null;
	private String BANCO = null;
	private Integer ID;

	public String getSubnucleo() {
		return this.SUBNUCLEO;
	}
	
	public void  setSubnucleo(String subnucleo) {
		this.SUBNUCLEO = subnucleo;
	}
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer ID) {
		this.ID = ID;
	}

	public String getPALAVRACHAVE() {
		return PALAVRACHAVE;
	}

	public void setPALAVRACHAVE(String PALAVRACHAVE) {
		this.PALAVRACHAVE = PALAVRACHAVE;
	}

	public String getCOMPLEMENTO() {
		return COMPLEMENTO;
	}

	public void setCOMPLEMENTO(String COMPLEMENTO) {
		this.COMPLEMENTO = COMPLEMENTO;
	}

	public String getETIQUETA() {
		return ETIQUETA;
	}

	public void setETIQUETA(String ETIQUETA) {
		this.ETIQUETA = ETIQUETA;
	}

	public String getTIPO() {
		return TIPO;
	}

	public void setTIPO(String TIPO) {
		this.TIPO = TIPO;
	}

	public String getPRIORIDADE() {
		return PRIORIDADE;
	}

	public void setPRIORIDADE(String PRIORIDADE) {
		this.PRIORIDADE = PRIORIDADE;
	}

	public String getBANCO() {
		return BANCO;
	}

	public void setBANCO(String BANCO) {
		this.BANCO = BANCO;
	}

}