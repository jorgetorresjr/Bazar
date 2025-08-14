package com.Bazar.doacoes.controllers;

import com.Bazar.doacoes.model.entities.Lote;
import com.Bazar.doacoes.model.entities.OrgaoDonatario;
import com.Bazar.doacoes.model.entities.OrgaoFiscalizador;
import com.Bazar.doacoes.model.entities.Produto;
import com.Bazar.doacoes.model.repositories.RepositorioFachada;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping({"/menu"})
public class ControladorMenu {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;
    @Autowired
    private RepositorioFachada fachada;

    private String __msg = null;

    @GetMapping("")
    public String menu(Model m) {
        try {
            List<Produto> produtos = fachada.readAllProdutos();
            m.addAttribute("produtos", produtos);

            m.addAttribute("orgaosDonatarios", fachada.readAllOrgaosDonatarios());
            m.addAttribute("orgaosFiscalizadores", fachada.readAllOrgaosFiscalizadores());

        } catch (Exception e) {
            m.addAttribute("produtos", List.of());
            m.addAttribute("orgaosDonatarios", List.of());
            m.addAttribute("orgaosFiscalizadores", List.of());
        }

        m.addAttribute("lote", new Lote());
        m.addAttribute("msg", this.__msg);
        session.removeAttribute("lotes");
        this.__msg = null;

        return "menu";
    }


    @PostMapping("/newProduto")
    public String newProduto(Model m, Produto p) {
        try {
            fachada.create(p);

            this.__msg = "Produto criado com sucesso!";
        } catch (ClassNotFoundException | SQLException e) {
            this.__msg = "Erro ao criar novo produto!";
        }

        return "redirect:/menu";
    }

    @GetMapping("/readProdutos")
    public String readAllProdutos(Model m) {
        try {
            List<Produto> produtos = fachada.readAllProdutos();
            m.addAttribute("produtos", produtos);

            return "produto/List";
        } catch (ClassNotFoundException | SQLException e) {
            this.__msg = "Erro ao listar produtos!";
            return "redirect:/menu";
        }

    }

    @GetMapping("/updateProduto/{codigo}")
    public String updateProdutoFind(Model m, @PathVariable int codigo) throws SQLException, ClassNotFoundException {
        Produto p = fachada.readProduto(codigo);
        m.addAttribute("produto", p);
        return "redirect:/menu/produtos";
    }

    @PostMapping("/updateProduto")
    public String updateProduto(Produto produto) {
        try {
            fachada.update(produto);
            __msg = "Produto atualizado com sucesso!";
        } catch (Exception e) {
            __msg = "Erro ao atualizar produto!";
        }
        return "redirect:/menu/produtos";
    }

    @PostMapping("/deleteProduto/{codigo}")
    public String deleteProduto(Model m, @PathVariable("codigo") int codigo) {
        try {
            fachada.delete(fachada.readProduto(codigo));

            this.__msg = "Produto deletado com sucesso!";
        } catch (ClassNotFoundException | SQLException e) {
            this.__msg = "Erro ao deletar produto!";
        }
        return "redirect:/menu/produtos";
    }

    @PostMapping("/newOrgaoFiscalizador")
    public String newOrgaoFiscalizador(Model m, OrgaoFiscalizador op) {
        try {
            fachada.create(op);

            this.__msg = "Órgão fiscalizador adicionado com sucesso!";

        } catch (ClassNotFoundException | SQLException e) {
            this.__msg = "Erro ao adicionar novo órgão fiscalizador!";
        }

        return "redirect:/menu";
    }

    @PostMapping("/newOrgaoDonatario")
    public String newOrgaoDonatario(Model m, OrgaoDonatario od) {
        try {
            fachada.create(od);
            this.__msg = "Órgão donatario adicionado com sucesso!";
        } catch (ClassNotFoundException | SQLException e) {
            this.__msg = "Erro ao adicionar novo órgão donatário!";
        }

        return "redirect:/menu";
    }

    @PostMapping("/updateOrgaoDonatario")
    public String updateOrgaoDonatario(OrgaoDonatario od) {
        try {
            fachada.update(od);
            __msg = "Órgão Donatário atualizado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            __msg = "Erro ao atualizar Órgão Donatário!";
        }
        return "redirect:/menu/orgaosDonatarios";
    }

    @PostMapping("/deleteOrgaoDonatario/{id}")
    public String deleteOrgaoDonatario(@PathVariable("id") int id) {
        try {
            OrgaoDonatario od = fachada.readOrgaoDonatario(id);
            fachada.delete(od);
            __msg = "Órgão Donatário deletado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            __msg = "Erro ao deletar Órgão Donatário!";
        }
        return "redirect:/menu/orgaosDonatarios";
    }


    @PostMapping("/lote")
    public String newLote(
            Lote lote,
            @RequestParam(value = "orgaoDonatarioId", required = false) Integer odId,
            @RequestParam(value = "orgaoFiscalizadorId", required = false) Integer ofId,
            @RequestParam(value = "produtosIds", required = false) List<Integer> produtosIds
    ) {
        try {
            if (odId != null) {
                OrgaoDonatario od = fachada.readOrgaoDonatario(odId);
                lote.setOrgaoDonatario(od);
            }

            if (ofId != null) {
                OrgaoFiscalizador of = fachada.readOrgaoFiscalizador(ofId);
                lote.setOrgaoFiscalizador(of);
            }

            if (produtosIds != null) {
                List<Produto> produtos = new ArrayList<>();
                for (Integer id : produtosIds) {
                    Produto p = fachada.readProduto(id); // Busca produto do DB
                    produtos.add(p);
                }
                lote.setProdutos(produtos);
            }

            fachada.create(lote);
            this.__msg = "Lote criado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            this.__msg = "Erro ao criar novo lote!";
        }

        return "redirect:/menu/lotes";
    }


    @RequestMapping("/orgaoDonatario")
    public String readOrgaoDonatario(Model m, @PathParam("id_orgao_donatario") int id) {
        return "redirect:/menu/lotes?orgaoDonatarioId=" + id;
    }

    @GetMapping("/orgaoFiscalizador")
    public String readOrgaoFiscalizador(@RequestParam("id_orgao_fiscalizador") int id) {
        return "redirect:/menu/lotes?orgaoFiscalizadorId=" + id;
    }


    @GetMapping("/lotes")
    public String listarLotes(
            Model m,
            @RequestParam(required = false) Integer orgaoDonatarioId,
            @RequestParam(required = false) Integer orgaoFiscalizadorId
    ) throws SQLException, ClassNotFoundException {
        List<Lote> lotes;

        if (orgaoDonatarioId != null) {
            lotes = fachada.readByOrgaoDonatario(orgaoDonatarioId);
        } else if (orgaoFiscalizadorId != null) {
            lotes = fachada.readByOrgaoFiscalizador(orgaoFiscalizadorId);
        } else {
            lotes = fachada.readAllLotes();
        }

        m.addAttribute("lotes", lotes);
        m.addAttribute("produtos", fachada.readAllProdutos());
        m.addAttribute("orgaosDonatarios", fachada.readAllOrgaosDonatarios());
        m.addAttribute("orgaosFiscalizadores", fachada.readAllOrgaosFiscalizadores());

        return "lotes";
    }


    @GetMapping("/produtos")
    public String listarProdutos(Model m) {
        try {
            List<Produto> produtos = fachada.readAllProdutos();
            m.addAttribute("produtos", produtos);
            return "produtos";
        } catch (Exception e) {
            __msg = "Erro ao listar produtos!";
            return "redirect:/menu";
        }
    }

    @GetMapping("/orgaosDonatarios")
    public String listarOrgaosDonatarios(Model m) {
        try {
            List<OrgaoDonatario> orgaos = fachada.readAllOrgaosDonatarios();
            m.addAttribute("orgaosDonatarios", orgaos);
            return "orgaosDonatarios";
        } catch (Exception e) {
            __msg = "Erro ao listar órgãos donatários!";
            return "redirect:/menu";
        }
    }

    @GetMapping("/orgaosFiscalizadores")
    public String listarOrgaosFiscalizadores(Model m) {
        try {
            List<OrgaoFiscalizador> orgaos = fachada.readAllOrgaosFiscalizadores();
            m.addAttribute("orgaosFiscalizadores", orgaos);
            return "orgaosFiscalizadores";
        } catch (Exception e) {
            __msg = "Erro ao listar órgãos fiscalizadores!";
            return "redirect:/menu";
        }
    }

    @GetMapping("/cadastro")
    public String mostrarFormulario(Model model) throws SQLException, ClassNotFoundException {
        model.addAttribute("orgaosDonatarios", fachada.readAllOrgaosDonatarios());
        model.addAttribute("orgaosFiscalizadores", fachada.readAllOrgaosFiscalizadores());
        return "formulario";
    }


}
