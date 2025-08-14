package com.Bazar.doacoes.model.repositories;

import com.Bazar.doacoes.model.entities.Lote;
import com.Bazar.doacoes.model.entities.OrgaoDonatario;
import com.Bazar.doacoes.model.entities.OrgaoFiscalizador;
import com.Bazar.doacoes.model.entities.Produto;

import java.sql.SQLException;
import java.util.List;

@org.springframework.stereotype.Repository
public class RepositorioFachada {

    private static RepositorioFachada myself = null;

    private Repositorio<Integer, Lote> rLote;
    private Repositorio<Integer, OrgaoFiscalizador> rOrgaoFiscalizador;
    private Repositorio<Integer, OrgaoDonatario> rOrgaoDonatario;
    private Repositorio<Integer, Produto> rProduto;

    public RepositorioFachada() {
        this.rLote = new RepositorioLote();
        this.rOrgaoDonatario = new RepositorioOrgaoDonatario();
        this.rOrgaoFiscalizador = new RepositorioOrgaoFiscalizador();
        this.rProduto = new RepositorioProduto();
    }

    public static RepositorioFachada getCurrentInstance() {
        if (myself == null) {
            myself = new RepositorioFachada();
        }
        return myself;
    }

    public void create(Lote lote) throws ClassNotFoundException, SQLException {
        this.rLote.create(lote);
    }

    public void update(Lote lote) throws ClassNotFoundException, SQLException {
        this.rLote.update(lote);
    }

    public void delete(Lote lote) throws ClassNotFoundException, SQLException {
        this.rLote.delete(lote);
    }

    public Lote read(int id) throws ClassNotFoundException, SQLException {
        return this.rLote.read(id);
    }

    public List<Lote> readAllLotes() throws ClassNotFoundException, SQLException {
        return this.rLote.readAll();
    }

    public List<Lote> readByOrgaoFiscalizador(int id) throws ClassNotFoundException, SQLException {
        return ((RepositorioLote)rLote).readByOrgaoFiscalizador(id);
    }

    public List<Lote> readByOrgaoDonatario(int id) throws ClassNotFoundException, SQLException {
        return ((RepositorioLote)rLote).readByOrgaoDonatario(id);
    }

    public void create(OrgaoDonatario od) throws ClassNotFoundException, SQLException {
        this.rOrgaoDonatario.create(od);
    }

    public void update(OrgaoDonatario od) throws ClassNotFoundException, SQLException {
        this.rOrgaoDonatario.update(od);
    }

    public void delete(OrgaoDonatario od) throws ClassNotFoundException, SQLException {
        this.rOrgaoDonatario.delete(od);
    }

    public OrgaoDonatario readOrgaoDonatario(int id) throws ClassNotFoundException, SQLException {
        return this.rOrgaoDonatario.read(id);
    }

    public List<OrgaoDonatario> readAllOrgaosDonatarios() throws ClassNotFoundException, SQLException {
        return this.rOrgaoDonatario.readAll();
    }

    public void create(Produto p) throws ClassNotFoundException, SQLException {
        this.rProduto.create(p);
    }

    public void update(Produto p) throws ClassNotFoundException, SQLException {
        this.rProduto.update(p);
    }

    public void delete(Produto p) throws ClassNotFoundException, SQLException {
        this.rProduto.delete(p);
    }

    public Produto readProduto(int id) throws ClassNotFoundException, SQLException {
        return this.rProduto.read(id);
    }

    public List<Produto> readAllProdutos() throws ClassNotFoundException, SQLException {
        return this.rProduto.readAll();
    }

    public void create(OrgaoFiscalizador of) throws ClassNotFoundException, SQLException {
        this.rOrgaoFiscalizador.create(of);
    }

    public void update(OrgaoFiscalizador of) throws ClassNotFoundException, SQLException {
        this.rOrgaoFiscalizador.update(of);
    }

    public void delete(OrgaoFiscalizador of) throws ClassNotFoundException, SQLException {
        this.rOrgaoFiscalizador.delete(of);
    }

    public OrgaoFiscalizador readOrgaoFiscalizador(int id) throws ClassNotFoundException, SQLException {
        return this.rOrgaoFiscalizador.read(id);
    }

    public List<OrgaoFiscalizador> readAllOrgaosFiscalizadores() throws ClassNotFoundException, SQLException {
        return this.rOrgaoFiscalizador.readAll();
    }


}
