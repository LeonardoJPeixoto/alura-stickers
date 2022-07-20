import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface Extrator {
    List<Conteudo> extrai(String json) throws JsonProcessingException;
}
