package com.Bazar.doacoes.model.repositories;

import com.Bazar.doacoes.model.entities.Lote;
import com.Bazar.doacoes.model.entities.OrgaoDonatario;
import com.Bazar.doacoes.model.entities.OrgaoFiscalizador;
import com.Bazar.doacoes.model.entities.Produto;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioLote implements Repositorio<Integer, Lote> {

    @Override
    public void create(Lote lote) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO lote(id, data_entrega, observacao, orgao_donatario_id, orgao_fiscalizador_id) VALUES (?,?,?,?,?)";

        try (PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setInt(1, lote.getId());
            pstm.setDate(2, new java.sql.Date(lote.getDataEntrega().getTime()));
            pstm.setString(3, lote.getObservacao());
            pstm.setInt(4, lote.getOrgaoDonatario().getId());
            pstm.setInt(5, lote.getOrgaoFiscalizador().getId());
            pstm.executeUpdate();

            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                lote.setId(rs.getInt(1));
            }
        }

        if (lote.getProdutos() != null) {
            String sqlRelacao = "INSERT INTO lote_produto(lote_id, produto_id) VALUES (?, ?)";
            try (PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sqlRelacao)) {
                for (Produto produto : lote.getProdutos()) {
                    pstm.setInt(1, lote.getId());
                    pstm.setInt(2, produto.getCodigo());
                    pstm.addBatch();
                }
                pstm.executeBatch();
            }
        }
    }


    @Override
    public void update(Lote l) throws ClassNotFoundException, SQLException {

        String sql = "UPDATE lote SET data_entrega=?, observacao=?, orgao_donatario_id=?, orgao_fiscalizador_id=? WHERE id=?";
        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);
        pstm.setDate(1, new java.sql.Date(l.getDataEntrega().getTime()));
        pstm.setString(2, l.getObservacao());
        pstm.setInt(3, l.getOrgaoDonatario().getId());
        pstm.setInt(4, l.getOrgaoFiscalizador().getId());
        pstm.setInt(5, l.getId());
        pstm.executeUpdate();

        String sqlDel = "DELETE FROM lote_produto WHERE lote_id=?";
        PreparedStatement pstmDel = ConnectionManager.getCurrentConnection().prepareStatement(sqlDel);
        pstmDel.setInt(1, l.getId());
        pstmDel.executeUpdate();

        if (l.getProdutos() != null && !l.getProdutos().isEmpty()) {
            String sqlLote_produto = "INSERT INTO lote_produto (lote_id, produto_id) VALUES (?, ?)";
            PreparedStatement pstmLote_produto = ConnectionManager.getCurrentConnection().prepareStatement(sqlLote_produto);
            for (Produto p : l.getProdutos()) {
                pstmLote_produto.setInt(1, l.getId());
                pstmLote_produto.setInt(2, p.getCodigo());
                pstmLote_produto.addBatch();
            }
            pstmLote_produto.executeBatch();
        }
    }


    @Override
    public Lote read(Integer k) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM lote WHERE id=?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);
        pstm.setInt(1, k);

        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            Lote l = new Lote();
            l.setId(rs.getInt("id"));
            l.setDataEntrega(rs.getDate("data_entrega"));
            l.setObservacao(rs.getString("observacao"));

            OrgaoDonatario od = new OrgaoDonatario();
            od.setId(rs.getInt("orgao_donatario_id"));
            l.setOrgaoDonatario(od);

            OrgaoFiscalizador of = new OrgaoFiscalizador();
            of.setId(rs.getInt("orgao_fiscalizador_id"));
            l.setOrgaoFiscalizador(of);

            return l;
        }

        return null;
    }

    @Override
    public void delete(Lote l) throws ClassNotFoundException, SQLException {

        String sqlLote_produto = "DELETE FROM lote_produto WHERE lote_id=?";
        PreparedStatement pstmDel = ConnectionManager.getCurrentConnection().prepareStatement(sqlLote_produto);
        pstmDel.setInt(1, l.getId());
        pstmDel.executeUpdate();

        String sql = "DELETE FROM lote WHERE id=?";
        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);
        pstm.setInt(1, l.getId());
        pstm.executeUpdate();
    }


    @Override
    public List<Lote> readAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM lote";
        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<Lote> lotes = new ArrayList<>();
        RepositorioProduto repoProduto = new RepositorioProduto();
        RepositorioOrgaoDonatario repoOd = new RepositorioOrgaoDonatario();
        RepositorioOrgaoFiscalizador repoOf = new RepositorioOrgaoFiscalizador();

        while (rs.next()) {
            Lote lote = new Lote();
            lote.setId(rs.getInt("id"));
            lote.setDataEntrega(rs.getDate("data_entrega"));
            lote.setObservacao(rs.getString("observacao"));

            int odId = rs.getInt("orgao_donatario_id");
            if (!rs.wasNull()) {
                lote.setOrgaoDonatario(repoOd.read(odId));
            }

            int ofId = rs.getInt("orgao_fiscalizador_id");
            if (!rs.wasNull()) {
                lote.setOrgaoFiscalizador(repoOf.read(ofId));
            }

            String sqlProdutos = "SELECT produto_id FROM lote_produto WHERE lote_id = ?";
            PreparedStatement pstmProd = ConnectionManager.getCurrentConnection().prepareStatement(sqlProdutos);
            pstmProd.setInt(1, lote.getId());
            ResultSet rsProd = pstmProd.executeQuery();

            List<Produto> produtos = new ArrayList<>();
            while (rsProd.next()) {
                int codigoProduto = rsProd.getInt("produto_id");
                Produto produto = repoProduto.read(codigoProduto);
                if (produto != null) {
                    produtos.add(produto);
                }
            }
            lote.setProdutos(produtos);

            lotes.add(lote);
        }
        return lotes;
    }


    public List<Lote> readByOrgaoDonatario(int id_orgao_donatario) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM lote WHERE orgao_donatario_id=?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        pstm.setInt(1, id_orgao_donatario);

        ResultSet rs = pstm.executeQuery();

        List<Lote> lotes = new ArrayList<>();

        RepositorioProduto repoProduto = new RepositorioProduto();
        RepositorioOrgaoDonatario repoOd = new RepositorioOrgaoDonatario();
        RepositorioOrgaoFiscalizador repoOf = new RepositorioOrgaoFiscalizador();

        while (rs.next()) {
            Lote l = new Lote();

            l.setId(rs.getInt("id"));
            l.setDataEntrega(rs.getDate("data_entrega"));
            l.setObservacao(rs.getString("observacao"));

            OrgaoDonatario od = repoOd.read(rs.getInt("orgao_donatario_id"));
            l.setOrgaoDonatario(od);

            OrgaoFiscalizador of = repoOf.read(rs.getInt("orgao_fiscalizador_id"));
            l.setOrgaoFiscalizador(of);

            String sqlProdutos = "SELECT produto_id FROM lote_produto WHERE lote_id = ?";
            PreparedStatement pstmProd = ConnectionManager.getCurrentConnection().prepareStatement(sqlProdutos);
            pstmProd.setInt(1, l.getId());
            ResultSet rsProd = pstmProd.executeQuery();

            List<Produto> produtos = new ArrayList<>();
            while (rsProd.next()) {
                int codigoProduto = rsProd.getInt("produto_id");
                Produto p = repoProduto.read(codigoProduto);
                if (p != null) produtos.add(p);
            }
            l.setProdutos(produtos);

            lotes.add(l);
        }
        return lotes;
    }

    public List<Lote> readByOrgaoFiscalizador(int id_orgao_fiscalizador) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM lote WHERE orgao_fiscalizador_id=?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        pstm.setInt(1, id_orgao_fiscalizador);

        ResultSet rs = pstm.executeQuery();

        List<Lote> lotes = new ArrayList<>();
        RepositorioProduto repoProduto = new RepositorioProduto();
        RepositorioOrgaoDonatario repoOd = new RepositorioOrgaoDonatario();
        RepositorioOrgaoFiscalizador repoOf = new RepositorioOrgaoFiscalizador();

        while (rs.next()) {
            Lote l = new Lote();

            l.setId(rs.getInt("id"));
            l.setDataEntrega(rs.getDate("data_entrega"));
            l.setObservacao(rs.getString("observacao"));

            OrgaoDonatario od = repoOd.read(rs.getInt("orgao_donatario_id"));
            l.setOrgaoDonatario(od);

            OrgaoFiscalizador of = repoOf.read(rs.getInt("orgao_fiscalizador_id"));
            l.setOrgaoFiscalizador(of);

            String sqlProdutos = "SELECT produto_id FROM lote_produto WHERE lote_id = ?";
            PreparedStatement pstmProd = ConnectionManager.getCurrentConnection().prepareStatement(sqlProdutos);
            pstmProd.setInt(1, l.getId());
            ResultSet rsProd = pstmProd.executeQuery();

            List<Produto> produtos = new ArrayList<>();
            while (rsProd.next()) {
                int codigoProduto = rsProd.getInt("produto_id");
                Produto p = repoProduto.read(codigoProduto);
                if (p != null) produtos.add(p);
            }
            l.setProdutos(produtos);

            lotes.add(l);
        }
        return lotes;
    }


}
