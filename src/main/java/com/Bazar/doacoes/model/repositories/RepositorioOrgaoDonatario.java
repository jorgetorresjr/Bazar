package com.Bazar.doacoes.model.repositories;

import com.Bazar.doacoes.model.entities.OrgaoDonatario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioOrgaoDonatario implements Repositorio<Integer, OrgaoDonatario> {
    @Override
    public void create(OrgaoDonatario od) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO orgao_donatario (nome, endereco, telefone, horario_funcionamento, descricao) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        pstm.setString(1, od.getNome());
        pstm.setString(2, od.getEndereco());
        pstm.setString(3, od.getTelefone());
        pstm.setString(4, od.getHorarioFuncionamento());
        pstm.setString(5, od.getDescricao());

        pstm.execute();
    }

    @Override
    public void update(OrgaoDonatario od) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE orgao_donatario SET nome=?, endereco=?, telefone=?, horario_funcionamento=?, descricao=?"
        + " WHERE id=?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        pstm.setString(1, od.getNome());
        pstm.setString(2, od.getEndereco());
        pstm.setString(3, od.getTelefone());
        pstm.setString(4, od.getHorarioFuncionamento());
        pstm.setString(5, od.getDescricao());
        pstm.setInt(6, od.getId());

        pstm.execute();
    }

    @Override
    public OrgaoDonatario read(Integer k) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM orgao_donatario WHERE id=?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        pstm.setInt(1, k);

        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            OrgaoDonatario od = new OrgaoDonatario();

            od.setId(rs.getInt("id"));
            od.setNome(rs.getString("nome"));
            od.setEndereco(rs.getString("endereco"));
            od.setTelefone(rs.getString("telefone"));
            od.setHorarioFuncionamento(rs.getString("horario_funcionamento"));
            od.setDescricao(rs.getString("descricao"));

            return od;
        }

        return null;
    }

    @Override
    public void delete(OrgaoDonatario od) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM orgao_donatario WHERE id=?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        pstm.setInt(1, od.getId());

        pstm.execute();
    }

    @Override
    public List<OrgaoDonatario> readAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM orgao_donatario";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);

        ResultSet rs = pstm.executeQuery();

        List<OrgaoDonatario> orgaosDonatarios = new ArrayList<OrgaoDonatario>();

        while (rs.next()) {
            OrgaoDonatario od = new OrgaoDonatario();
            od.setId(rs.getInt("id"));
            od.setNome(rs.getString("nome"));
            od.setEndereco(rs.getString("endereco"));
            od.setTelefone(rs.getString("telefone"));
            od.setHorarioFuncionamento(rs.getString("horario_funcionamento"));
            od.setDescricao(rs.getString("descricao"));

            orgaosDonatarios.add(od);
        }
        return orgaosDonatarios;
    }
}
