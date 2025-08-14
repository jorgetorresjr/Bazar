package com.Bazar.doacoes.model.repositories;

import com.Bazar.doacoes.model.entities.OrgaoFiscalizador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioOrgaoFiscalizador implements Repositorio<Integer, OrgaoFiscalizador> {
    @Override
    public void create(OrgaoFiscalizador of) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO orgao_fiscalizador (nome, descricao) VALUES (?, ?)";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        pstm.setString(1, of.getNome());
        pstm.setString(2, of.getDescricao());
        pstm.execute();
    }

    @Override
    public void update(OrgaoFiscalizador c) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE orgao_fiscalizador SET nome = ?, descricao = ? WHERE id = ?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        pstm.setString(1, c.getNome());
        pstm.setString(2, c.getDescricao());
        pstm.setInt(3, c.getId());

        pstm.execute();
    }

    @Override
    public OrgaoFiscalizador read(Integer k) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM orgao_fiscalizador WHERE id = ?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        pstm.setInt(1, k);

        ResultSet rs = pstm.executeQuery();

        OrgaoFiscalizador of = null;

        if (rs.next()) {
            of = new OrgaoFiscalizador();

            of.setId(rs.getInt("id"));
            of.setNome(rs.getString("nome"));
            of.setDescricao(rs.getString("descricao"));

            return of;
        }

        return null;
    }

    @Override
    public void delete(OrgaoFiscalizador of) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM orgao_fiscalizador WHERE id = ?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        pstm.setInt(1, of.getId());

        pstm.execute();
    }

    @Override
    public List<OrgaoFiscalizador> readAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM orgao_fiscalizador";
        ResultSet rs = ConnectionManager.getCurrentConnection().prepareStatement(sql).executeQuery();

        List<OrgaoFiscalizador> lista = new ArrayList<OrgaoFiscalizador>();

        while (rs.next()) {
            OrgaoFiscalizador of = new OrgaoFiscalizador();

            of.setId(rs.getInt("id"));
            of.setNome(rs.getString("nome"));
            of.setDescricao(rs.getString("descricao"));

            lista.add(of);
        }
        return lista;
    }
}
