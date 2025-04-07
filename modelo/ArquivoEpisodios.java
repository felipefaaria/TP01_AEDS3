package modelo;

import java.io.File;
import java.util.ArrayList;

import aed3.*;
import classes.Episodio;

public class ArquivoEpisodios extends Arquivo<Episodio> {

  Arquivo<Episodio> arqEpisodios;
  ArvoreBMais<ParNomeId> indiceNome;
  ArvoreBMais<ParIdId> indiceRelacaoSerieEp;

  public ArquivoEpisodios() throws Exception {

    super("episodios", Episodio.class.getConstructor());

    File directory = new File("./dados/episodio");
    if (!directory.exists()) {
      directory.mkdirs();
    }

    indiceNome = new ArvoreBMais<>(
        ParNomeId.class.getConstructor(), 5, "./dados/episodio" + "/indiceNome.db");

    indiceRelacaoSerieEp = new ArvoreBMais<>(
        ParIdId.class.getConstructor(), 5, "./dados/episodio" + "/indiceRelacaoSerieEp.db");

  }

  @Override
  public int create(Episodio ep) throws Exception {

    int id = super.create(ep);

    indiceNome.create(new ParNomeId(ep.getNome(), id));

    indiceRelacaoSerieEp.create(new ParIdId(ep.getIDSerie(), id));

    return id;

  }

  public Episodio[] readNome(String nome) throws Exception {

    if (nome.length() == 0)
      return null;

    ArrayList<ParNomeId> pares = indiceNome.read(new ParNomeId(nome, -1));

    if (pares.size() > 0) {

      Episodio[] episodios = new Episodio[pares.size()];

      int i = 0;

      for (ParNomeId par : pares) {

        episodios[i++] = read(par.getId());

      }

      return episodios;

    } else {

      return null;
    }

  }

  @Override
  public boolean delete(int id) throws Exception {

    Episodio ep = read(id);

    if (ep != null) {

      if (super.delete(id)) {

        return indiceNome.delete(new ParNomeId(ep.getNome(), id))
            && indiceRelacaoSerieEp.delete(new ParIdId(ep.getIDSerie(), id));

      }

    }

    return false;

  }

  @Override
  public boolean update(Episodio novoEpisodio) throws Exception {

    Episodio ep = read(novoEpisodio.getID());

    if (ep != null) {

      if (super.update(novoEpisodio)) {

        if (!ep.getNome().equals(novoEpisodio.getNome())) {

          indiceNome.delete(new ParNomeId(ep.getNome(), ep.getID()));
          indiceNome.create(new ParNomeId(novoEpisodio.getNome(), novoEpisodio.getID()));
        }
        return true;
      }

    }
    return false;
  }

  public Episodio[] readPorSerie(int idSerie) throws Exception {

    ArrayList<ParIdId> pares = indiceRelacaoSerieEp.read(new ParIdId(idSerie, -1));

    if (pares.isEmpty()) {
      return null;
    }

    Episodio[] episodios = new Episodio[pares.size()];
    int i = 0;

    for (ParIdId par : pares) {

      episodios[i++] = read(par.getId2());

    }

    return episodios;

  }

}