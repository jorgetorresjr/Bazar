package com.Bazar.doacoes.model.repositories;

import com.Bazar.doacoes.model.entities.Produto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioProduto implements Repositorio<Integer, Produto> {
    @Override
    public void create(Produto produto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO produto(codigo, nome, descricao) VALUES(?,?,?)";

        try (PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql)) {
            pstm.setInt(1, produto.getCodigo());
            pstm.setString(2, produto.getNome());
            pstm.setString(3, produto.getDescricao());
            int rowsAffected = pstm.executeUpdate();
            System.out.println("Linhas inseridas: " + rowsAffected);
        }
    }

    @Override
    public void update(Produto p) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE produto SET nome = ?, descricao = ? WHERE codigo = ?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);
        pstm.setString(1, p.getNome());
        pstm.setString(2, p.getDescricao());
        pstm.setInt(3, p.getCodigo());
        pstm.execute();
    }

    @Override
    public Produto read(Integer k) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM produto WHERE codigo = ?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);
        pstm.setInt(1, k);
        ResultSet rs = pstm.executeQuery();

        Produto produto = null;

        if (rs.next()) {
            produto = new Produto();

            produto.setCodigo(k);
            produto.setNome(rs.getString("nome"));
            produto.setDescricao(rs.getString("descricao"));

            return produto;
        }

        return null;
    }

    @Override
    public void delete(Produto p) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM produto WHERE codigo = ?";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);
        pstm.setInt(1, p.getCodigo());
        pstm.execute();
    }

    @Override
    public List<Produto> readAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM produto";

        PreparedStatement pstm = ConnectionManager.getCurrentConnection().prepareStatement(sql);
        ResultSet res = pstm.executeQuery();

        Produto produto = null;
        List<Produto> produtos = new ArrayList<>();

        while (res.next()) {
            produto = new Produto();

            produto.setCodigo(res.getInt("codigo"));
            produto.setNome(res.getString("nome"));
            produto.setDescricao(res.getString("descricao"));
            produtos.add(produto);
        }

        return produtos;
    }

//    public static void main(String args[]) throws SQLException, ClassNotFoundException {
//
//        RepositorioProduto pr = new RepositorioProduto();
//
//        try {
//            Produto p = new Produto();
//            p.setCodigo(4);
//            p.setNome("Shampuuuu");
//            p.setDescricao("Não tem");
//            pr.create(p);
//
//            p = new Produto();
//            p.setCodigo(1);
//            p.setNome("Condicionador");
//            p.setDescricao("Não tem");
//            pr.create(p);
//
//            p = new Produto();
//            p.setCodigo(2);
//            p.setNome("Creme Dental");
//            p.setDescricao("Não tem");
//            pr.create(p);
//
//            p = new Produto();
//            p.setCodigo(3);
//            p.setNome("Lixa de unha");
//            p.setDescricao("Não tem");
//            pr.create(p);
//
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//
//        // Agora lista tudo depois das inserções
//        List<Produto> produtos = pr.readAll();
//        for (Produto prod : produtos) {
//            System.out.println("Produto: " + prod.getCodigo() + " - " + prod.getNome() + " - " + prod.getDescricao());
//        }
//    }
}