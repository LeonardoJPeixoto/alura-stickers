import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

public class GeradoraDeFigurinha {

    public void cria(InputStream inputStream, String titulo) throws Exception {

        if (inputStream == null || titulo == null) {
            return;
        }

        // leitura da Imagem
        BufferedImage imagemOriginal = ImageIO.read(inputStream);
        if (imagemOriginal == null) {
            return;
        }

        // cria nova imagem em memória com transparência e com novo tamanho
        int largura = imagemOriginal.getWidth();
        int altura = imagemOriginal.getHeight();
        int novaAltura = altura + 200;
        BufferedImage novaImagem = new BufferedImage(largura, novaAltura, BufferedImage.TRANSLUCENT);

        // copiar a imagem original pra nova imagem (em memória)
        Graphics graphics = novaImagem.createGraphics();
        graphics.drawImage(imagemOriginal, 0, 0, null);

        // escrever uma frase na nova imagem
        var fonte = new Font(Font.SANS_SERIF, Font.BOLD, 32);
        graphics.setFont(fonte);
        graphics.setColor(Color.YELLOW);
        graphics.drawString("TOPZERA", 0, novaAltura - 100);

        // escrever a nova imagem em um arquivo
        String nomeArquivo = titulo
                .replace(":", "")
                .replace("?", "")
                .replace("!", "")
                .replace("\n", " ")
                .replace("\r", "")
                .concat(".png");
        ImageIO.write(novaImagem, "png", new File("saida/" + nomeArquivo));

    }
}
