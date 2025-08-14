package com.Bazar.doacoes.model.entities;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Lote {
    private int id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dataEntrega;

    private String observacao;

    private OrgaoFiscalizador orgaoFiscalizador;
    private OrgaoDonatario orgaoDonatario;
    private List<Produto> produtos;

    public Lote() {
        produtos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public OrgaoFiscalizador getOrgaoFiscalizador() {
        return orgaoFiscalizador;
    }

    public void setOrgaoFiscalizador(OrgaoFiscalizador orgaoFiscalizador) {
        this.orgaoFiscalizador = orgaoFiscalizador;
    }

    public OrgaoDonatario getOrgaoDonatario() {
        return orgaoDonatario;
    }

    public void setOrgaoDonatario(OrgaoDonatario orgaoDonatario) {
        this.orgaoDonatario = orgaoDonatario;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
