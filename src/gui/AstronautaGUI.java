/*********************************************************
 *  @file: AstronautaGUI.java
 *  @author: Erasmo de Castro Leite Junior
 *  @ : erasmo@astronautdatabase.com
 * *******************************************************/

package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import crud.AstronautaCreate;
import dao.AstronautaDAO;
import dao.ConnectionFactory;
import modelo.Astronauta;
import modelo.JListaDeAstronautas;
import modelo.JListaDePaises;
import modelo.Pais;
import swingHelper.StatusBar;
import utility.FormatadorDeImagem;

@SuppressWarnings("serial")
public class AstronautaGUI extends JFrame implements ListSelectionListener {

	private Connection 						con;
	private static ArrayList<Astronauta> 	astronautas;
	private static ArrayList<Pais> 			paises;
	private JListaDeAstronautas 			jlistaDeAstronautas; 						// caixa de lista p/ escolha nome
	private JListaDePaises					jlistaDePaises;								// caixa de lista p/ escolha pais
	private JTextArea 						taInfo; 									// areas de texto p/ info astronauta
	private JTextArea 						taInfoBio;
	@SuppressWarnings("unused")
	private JTextArea					    taInfoConnection;							// propriedades da conexÃ£o
	private JLabel                          lbl_foto; 									// lbl_foto que contem a foto do astronauta
	private JLabel                          lbl_bandeira;                               // lbl_bandeira contem a bandeira do pais

	private final File arqFonte = new File ("./fontes/Spaceport.ttf");

	private static final int 	larguraJanela = 140;			// o tamanho da janela da foto
	private static final int 	alturaJanela = larguraJanela * 3/2;

	private static final String[] sArquivo = {
			"Novo","new16.gif","N",
			"Abrir ...","open16new.png","A",
			"Salvar","save16new.png","S",
			"Salvar como ...","saveas16new.png","c",
			null, null, null,
			"Imprimir ...","print16new.png","I",
			null, null, null,
			"Sair","exit16new.png","r"};

	private static final String[] sEditar = {
			"Recortar","cut16.png","R",
			"Copiar","copy16new.png","C",
			"Colar","paste16.gif","o",
			null, null, null,
			"Excluir","delete16.gif","x",
			"Selecionar tudo","blank16.gif","t"};

	private static final String[] sBanco = {
			"Exportar","database-export.png","E",
			"Importar","database-import.png","m",
			"Nuvem","database-cloud.png","v",
			"Rede","database-network.png","R",
			null, null, null,
			"Estatisticas","database-property.png","s",
			null, null, null,
			"Editar registro", "database--pencil.png", "d",
			"Inserir registro","database-insert.png","I",
			"Remover registro", "database-delete.png","e",
			null, null, null,
			"Preparar Imagens", "shuttle.png","I",
			"Testa Funcoes Especiais", "yin-yang.png", "i"};

	private static final String[] sPais = {
			"Todos","ALL.png","T",
			"Afeganistao","AFG.png","a",
			"Africa do Sul","ZAF.png","f",
			"Alemanha","DEU.png","A",
			"Arabia Saudita","SAU.png","t",
			"Austria","AUT.png","u",
			"Belgica","BEL.png", "L",
			"Brasil","BRA.png","B",
			"Bulgaria","BGR.png","i",
			"Canada","CAN.png","d",
			"Casaquistao","KAZ.png","z",
			"China","CHN.png","C",
			"Coreia do Sul","KOR.png","K",
			"Cuba","CUB.png","b",
			"Eslovaquia","SVK.png","q",
			"Estados Unidos","USA.png","E",
			"Franca","FRA.png","F",
			"Holanda","NLD.png","l",
			"Hungria","HUN.png","H",
			"India","IND.png", "n",
			"Israel","ISR.png","r",
			"Italia","ITA.png","I",
			"Japao","JPN.png","J",
			"Malasia","MYS.png","M",
			"Mexico","MEX.png","x",
			"Mongolia","MNG.png","g",
			"Polonia","POL.png","P",
			"Rep. Checa","CZE.png","h",
			"Reino Unido","GBR.png","U",
			"Romenia","ROU.png","o",
			"Russia","RUS.png","R",
			"Siria","SYR.png","y",
			"Suecia","SWE.png","S",
			"Suica","CHE.png","Ã§",
			"Ucrania","UKR.png","k",
			"Vietnam","VNM.png","V",
	};

	private static final String[] sGrupo = {
			" Todos","globe-green.png","o",
			" Astronautas","hamburger.png","A",
			" Cosmonautas","wall.png", "C",
			" Taikonautas","yin-yang.png","k",
			" Turistas","money-bag-dollar.png","T"
	};

	private static final String[] sOrdenar = {
			" Ordem de Viagem","shuttle.png","V",
			" Sobrenome","address-book.png","n",
			" Data de Nascimento","calendar-day.png","D",
			" Numero de Missoes","counter.png", "M",
			" Cidade de Nascimento","building-hedge.png","C",
			" Tempo no Espaco","alarm-clock.png","Ã§",
			null,null,null,
			" Inverter Ordem","arrow-return-090.png","I"
	};

	private static final String[] sAjuda = {
			"Ajuda","help16new.png","A", null, null, null,
			"Sobre ...","about16new.png","S"};

	private String 	strSexo = "ALL",
			strMissao = "ALL",
			strPais = "ALL",
			strDataNasc = "ALL",
			strGrupo = "ALL";

	private String ConProperties;
	private String Ordem = sOrdenar[0];

	private final StatusBar statusBar = new StatusBar();

	private static ArrayList<Astronauta> getAstronautas() {
		return astronautas;
	}

	private static void setAstronautas(ArrayList<Astronauta> astronautas) {
		AstronautaGUI.astronautas = astronautas;
	}

	public Connection getCon() {
		return con;
	}

	private void setCon(Connection con) {
		this.con = con;
	}

	private static ArrayList<Pais> getPaises() {
		return paises;
	}

	private static void setPaises(ArrayList<Pais> paises) {
		AstronautaGUI.paises = paises;
	}

	private JListaDeAstronautas getListaDeAstronautas() {
		return jlistaDeAstronautas;
	}

	public void setListaDeAstronautas(JListaDeAstronautas listaDeAstronautas) {
		this.jlistaDeAstronautas = listaDeAstronautas;
	}

	public JListaDePaises getListaDePaises() {
		return jlistaDePaises;
	}

	public void setListaDePaises(JListaDePaises listaDePaises) {
		this.jlistaDePaises = listaDePaises;
	}

	public String getStrDataNasc() {
		return strDataNasc;
	}

	public void setStrDataNasc(String strDataNasc) {
		this.strDataNasc = strDataNasc;
	}

	public String getStrMissao() {
		return strMissao;
	}

	public void setStrMissao(String strMissao) {
		this.strMissao = strMissao;
	}

	private String getStrPais() {
		return strPais;
	}

	private void setStrPais(String strPais) {
		this.strPais = strPais;
	}

	private String getStrSexo() {
		return strSexo;
	}

	private void setStrSexo(String strSexo) {
		this.strSexo = strSexo;
	}

	private String getStrGrupo() {
		return strGrupo;
	}

	private void setStrGrupo(String strGrupo) {
		this.strGrupo = strGrupo;
	}

	public String getOrdem() {
		return Ordem;
	}

	private void setOrdem(String ordem) {
		Ordem = ordem;
	}

	/***********************************
	 *  Construtor da Interface Grafica
	 ***********************************/

	private AstronautaGUI() throws SQLException, IOException {
		super("Viajantes Espaciais - The Astronaut & Cosmonaut Database"); // ajusta titulo
		setIconImage(new ImageIcon("./imagens/vetor/astronaut-icon.png").getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // acao fechar
		// setLocationRelativeTo(null);

		inicializa();
	}

	/**
	 * CRIACAO DOS MENUS
	 */
	private void criaMenu() {

		JMenuBar mb = new JMenuBar();
		MenuHandler mh = new MenuHandler();
		MenuSexoHandler mrh = new MenuSexoHandler();
		MenuPaisHandler mcbh = new MenuPaisHandler();
		MenuGrupoHandler mth = new MenuGrupoHandler();

		MenuBuilder.imagePrefix = "./imagens/vetor/";

		/*****************************************************************************************
        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> MENU ARQUIVO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        Permite manipular arquivos(AINDA NÃO IMPLEMENTADA!!!).
		 ******************************************************************************************/

		mb.add(MenuBuilder.newMenu("Arquivo", 'A', sArquivo, mh));


		/*****************************************************************************************
        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> MENU EDITAR <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        Permite selecionar e copiar dados para a área de transferencia(AINDA NÃO IMPLEMENTADA!!!).
		 ******************************************************************************************/

		mb.add(MenuBuilder.newMenu("Editar", 'E', sEditar, mh));


		/*****************************************************************************************
        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> MENU FERRAMENTAS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        Permite acessar ferramentas de tratamento de imagem, atualização de dados e outros.
		 ******************************************************************************************/

		MenuBuilder.imagePrefix = "./imagens/vetor/";
		JMenu menuTools = new JMenu("Ferramentas");
		menuTools.setMnemonic('t');
		//menuTools.setIcon(new ImageIcon(MenuBuilder.imagePrefix + "Toolbox.png"));

		JMenu menuBancoDeDados = MenuBuilder.newMenu("Banco de Dados", 'D', sBanco, mh);
		menuBancoDeDados.setIcon(new ImageIcon(MenuBuilder.imagePrefix + "database.png"));
		menuTools.add(menuBancoDeDados);

		JMenuItem miTrataImagem = new JMenuItem("Preparar Imagens");
		miTrataImagem.setIcon(new ImageIcon(MenuBuilder.imagePrefix + "shuttle.png"));

		menuTools.addSeparator();
		menuTools.add(miTrataImagem);

		mb.add(menuTools);

		/*****************************************************************************************
        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> MENU FILTRAR <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        Permite acessar ferramentas de tratamento de imagem, atualização de dados e outros.
		 ******************************************************************************************/

		JMenu menuFiltrar = new JMenu("Filtrar");

		/*
         ITENS DO MENU FILTRAR - missao, data de nascimento, parte do nome
         Seleciona astronauta com base em filtros: nome, pais, missao, data de nascimento.
		 */

		JMenuItem miMissao = new JMenuItem("Missao");
		miMissao.setIcon(new ImageIcon("./imagens/vetor/shuttle.png"));
		miMissao.addActionListener(mh);
		JMenuItem miDataNasc = new JMenuItem("Data de Nascimento");
		miDataNasc.setIcon(new ImageIcon("./imagens/vetor/calendar-day.png"));
		JMenuItem miNome = new JMenuItem("Parte do Nome");
		miNome.setIcon(new ImageIcon("./imagens/vetor/document-attribute.png"));
		miNome.addActionListener(mh);

		/*
        ITENS DO MENU FILTRAR - ordenar seleção
        Ordena a lista selecionada com base em filtros: nome, pais, missao, data de nascimento.
		 */

		JMenuItem miOrdena = new JMenuItem("Ordena selecao por...");
		miOrdena.setIcon(new ImageIcon("./imagens/vetor/sort-alphabet.png"));
		JMenuItem miAtualiza = new JMenuItem("Atualiza");
		miAtualiza.addActionListener(mh);
		miAtualiza.setIcon(new ImageIcon("./imagens/vetor/arrow-circle.png"));

		/*
         ITENS DO MENU FILTRAR - sexo
         Seleciona astronauta com base em filtros: sexo masculino (M), feminino (F) ou ambos (ALL)
		 */

		JMenu menuSexo = new JMenu("Sexo");
		menuSexo.setMnemonic('x');
		menuSexo.setIcon(new ImageIcon("./imagens/vetor/gender.png"));
		JRadioButtonMenuItem miAmbos = new JRadioButtonMenuItem("Ambos", true);
		miAmbos.setIcon(new ImageIcon("./imagens/vetor/users.png"));
		JRadioButtonMenuItem miMale = new JRadioButtonMenuItem("Homens", false);
		miMale.setIcon(new ImageIcon("./imagens/vetor/user.png"));
		JRadioButtonMenuItem miFemale = new JRadioButtonMenuItem("Mulheres", false);
		miFemale.setIcon(new ImageIcon("./imagens/vetor/user-female.png"));

		miAmbos.addItemListener(mrh);
		miMale.addItemListener(mrh);
		miFemale.addItemListener(mrh);
		ButtonGroup bg = new ButtonGroup();
		bg.add(miAmbos);bg.add(miMale);bg.add(miFemale);

		menuSexo.add(miAmbos);menuSexo.add(miMale);menuSexo.add(miFemale);

		MenuBuilder.imagePrefix = "./imagens/flags/";
		JMenu menuPais = MenuBuilder.newRadioButtonMenu("Pais", 'P', sPais, mcbh);
		menuPais.setIcon(new ImageIcon(MenuBuilder.imagePrefix + "map.png"));

		MenuBuilder.imagePrefix = "./imagens/vetor/";
		JMenu menuGrupo = MenuBuilder.newRadioButtonMenu("Grupo", 'G', sGrupo, mth);
		menuGrupo.setIcon(new ImageIcon(MenuBuilder.imagePrefix + "block.png"));

		JMenu menuOrdenar = MenuBuilder.newMenu("Ordenar selecao por...", 'O', sOrdenar, mh);
		menuOrdenar.setIcon(new ImageIcon(MenuBuilder.imagePrefix + "sort-alphabet.png"));

		menuFiltrar.add(menuPais);
		menuFiltrar.add(menuSexo);
		menuFiltrar.add(menuGrupo);
		menuFiltrar.add(miMissao);
		menuFiltrar.add(miDataNasc);
		menuFiltrar.add(miNome);
		menuFiltrar.addSeparator();
		menuFiltrar.add(menuOrdenar);
		menuFiltrar.addSeparator();
		menuFiltrar.add(miAtualiza);
		menuFiltrar.setMnemonic('F');
		mb.add(menuFiltrar);
		mb.add(MenuBuilder.newMenu("Ajuda", 'u', sAjuda, mh));

		/********************************************************

          			Adiciona os menus na barra principal

		 ********************************************************/

		setJMenuBar(mb); //JMenu menu = mb.getMenu(1);

	}

	/**
	 * Cria paineis de divisao
	 * @throws SQLException
	 */
	private void criaPainel() throws SQLException {
		// painel de divisao
		JSplitPane split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				lbl_bandeira = new JLabel("", new ImageIcon(), JLabel.CENTER), // imagem
				new JScrollPane(jlistaDeAstronautas)); // diretorio

		JSplitPane split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,// painel de divisao interno
				lbl_foto = new JLabel(), // janela com a Foto do astronauta
				new JScrollPane(taInfo = new JTextArea())); // area de texto

		JSplitPane split3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				split2,
				new JScrollPane(taInfoBio = new JTextArea())); // area de texto

		JSplitPane split4 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				split1,
				split3);

		JSplitPane split5 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				split4,
				new JScrollPane(taInfoConnection = new JTextArea())); // area de info sobre conexÃ£o

		getContentPane().add(split5, "Center"); // adiciona a janela


		// outros ajustes
		Font fonte;
		try {
			fonte = Font.createFont(Font.TRUETYPE_FONT, arqFonte);
			fonte = fonte.deriveFont(Font.PLAIN, 14);

		} catch (FontFormatException | IOException e1) {
			fonte = new Font("Verdana", Font.BOLD, 14);
		}
		split1.setDividerLocation(133);
		split2.setBorder(BorderFactory.createLoweredBevelBorder());
		split2.setDividerLocation(larguraJanela);
		split2.setOneTouchExpandable(true);
		split3.setDividerLocation(alturaJanela);
		split4.setDividerLocation(200);
		split5.setDividerLocation(1000);

		/*****************************************************************************************
         PAINEIS COM INFORMACOES SOBRE O VIAJANTE
         Apresenta as informacoes do astronauta selecionado.
		 ******************************************************************************************/
		taInfo.setEditable(false);
		taInfo.setAutoscrolls(true);
		taInfoBio.setAutoscrolls(true);
		taInfoBio.setEditable(false);
		taInfo.setSelectionStart(0);
		taInfoBio.setSelectionStart(0);
		taInfo.setLineWrap(true);
		taInfo.setWrapStyleWord(true);
		taInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
		taInfoBio.setLineWrap(true);
		taInfoBio.setWrapStyleWord(true);
		taInfoBio.setFont(new Font("Segoe UI", Font.PLAIN, 10));

		setSize(1200, 720); // dimensiona janela

		jlistaDeAstronautas.addListSelectionListener(this);
		jlistaDeAstronautas.setSelectedIndex(0); // elemento inicial da lista

	}
	/**
	 * @throws HeadlessException
	 * @throws SQLException
	 */

	/**
	 * Conecta com o BD MySql
	 *
	 * @throws HeadlessException
	 * @throws SQLException
	 */
	private void consultaSQL() throws HeadlessException, SQLException {
		try (Connection con = ConnectionFactory.getConnection()){
			setCon(con);
			ConProperties = this.con.getMetaData().getURL();
			importaBD(con);
		}	catch (SQLException e){
			throw new RuntimeException(e);

		}
	}

	/*****************************************************************************************
                            IMPORTACAO DO BANCO DE DADOS MySQL

     Cria um  objeto de acesso a banco de dados (DAO) para carregar as listas de paises e
     astronautas.

	 ******************************************************************************************/
	/**
	 *
	 * @method importaBD
	 * @param connection connection
	 *
	 * Cria os ArrayLists:
	 *
	 * 1) astronautas 	- colecao de todos os astronautas recuperada do banco de dados MySql
	 * 2) paises 		- colecao de todos os paises recuperada do banco de dados MySql
	 *
	 */
	private void importaBD(Connection connection)
			throws SQLException {

		AstronautaDAO dao 	= new AstronautaDAO(connection);

		setAstronautas	(dao.pegaAstronautas());

		setPaises		(dao.pegaPaises());
	}

	/**
	 *
	 * Cria listas de astronautas e paises
	 */
	private void criaListas() {

		// CRIA LISTA DE ASTRONAUTAS E CARREGA NO JLIST
		jlistaDeAstronautas = new JListaDeAstronautas(getAstronautas());

		// CRIA LISTA DE PAISES E CARREGA NO JLIST
		jlistaDePaises = new JListaDePaises(getPaises());

	}

	/**
	 * @method mostraStatusListaAstro
	 * Mostra o estado atual da lista de astronautas
	 */
	private void mostraStatusListaAstro() {
		System.out.println("LISTA ASTRONAUTAS");
		System.out.println("Pais = " + getStrPais());
		System.out.println("Sexo = " + getStrSexo());
		System.out.println(jlistaDeAstronautas);
	}

	/********************************************

    		classe implementa LISTENER

	 *********************************************/

	@Override
	public void valueChanged(ListSelectionEvent e) {

		/* formatador de data */
		DateTimeFormatter dateTimeFormatter3 = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

		Astronauta selecionado;

		int i = getListaDeAstronautas().getSelectedIndex();
		if (i == -1) {
			selecionado = AstronautaDAO.NENHUM;
		} else {
			selecionado = getListaDeAstronautas().getSelectedValue();
		}

		/*****************************************************************************************
         AREA DE TEXTO COM INFO DO ASTRONAUTA
         Exibe informacoes do astronauta: ID, nome, pais de origem, data de nascimento, sexo.
		 ******************************************************************************************/

		StringBuilder sb = new StringBuilder("REGISTRO: \t" + selecionado.getIdAstronauta() + "\n");
		String nome = selecionado.getPrimeiro_Nome() + " "
				+ selecionado.getNome_do_Meio() + " "
				+ selecionado.getSobrenome() + "\n";
		String pais = jlistaDePaises.mostraNome(selecionado.getPais_Nasc(), paises) + "\n";
		String estado = selecionado.getEstado_Nasc() + "\n";
		String cidade = selecionado.getCidade_Nasc() + "\n";
		LocalDate dataNasc = selecionado.getDtNasc();
		String textDataNasc = dataNasc.format(dateTimeFormatter3);
		LocalDate dataFalec = selecionado.getDtFalec();
		String textDataFalec = dataFalec.format(dateTimeFormatter3);

		sb.append("NOME: \t").append(nome);
		sb.append("PAIS: \t").append(pais);
		sb.append("ESTADO: \t").append(estado);
		sb.append("CIDADE: \t").append(cidade);
		sb.append("NASCEU: \t").append(textDataNasc).append("\n");


		// caso a data de falecimento recuperada esteja no futuro
		// indica que o astronauta continua vivo --> nao imprime
		// data de falecimento.

		if (dataFalec.isBefore(LocalDate.now())) {         
			sb.append("FALECEU: \t").append(textDataFalec).append("\n");
		}

		sb.append("SEXO: \t").append(selecionado.getSexo().charAt(0) == 'M' ? "masculino" : "feminino").append("\n\n");

		StringBuilder sb2 = new StringBuilder("BIOGRAFIA: " + "\n\n" + selecionado.getInfo() + "\n\n");
		sb2.append("MISSOES: " + "\n\n");
		for (String s : selecionado.getMissao()) {
			sb2.append(s).append("\n");
		}

		taInfo.setText(sb.toString());

		taInfoBio.setText(sb2.toString());

		// inicializa statusBar
		atualizaStatusBar(statusBar);
		getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH); // adiciona barra de status

		/***************************************************
		 *            PREPARACAO DAS IMAGENS
		 ***************************************************/

		BufferedImage imagem = null;

		try {
			File entrada = new File("./imagens/people/" + selecionado.getFoto());
			imagem = ImageIO.read(entrada);

		} catch (IOException exc) {
			mostraMsgIOException();
		}

		lbl_foto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lbl_foto.setSize(larguraJanela, alturaJanela);
		assert imagem != null;
		lbl_foto.setIcon(new ImageIcon(imagem));

		// imagem da bandeira

		ImageIcon bandeiraVazia = new ImageIcon("./imagens/flag/lv.gif");
		ImageIcon bandeira = null;


		try {
			File entrada = new File("./imagens/flag_jpg/" + selecionado.getPais_Nasc() + ".jpg");
			imagem = ImageIO.read(entrada);
			bandeira = new ImageIcon(imagem);

		} catch (Exception exc) {
			bandeira = bandeiraVazia;
		}

		lbl_bandeira.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lbl_bandeira.setSize(200, 133);
		assert bandeira != null;
		lbl_bandeira.setIcon(bandeira);

	}

	/*****************************************
	 * Metodo antigo (usar se der pau!!!)
	 *****************************************/

	/*
        BufferedImage img = null, imagem = null;

            try {
                File entrada = new File("./imagens/people/" + astronauta.getFoto());
                img = ImageIO.read(entrada);

                imagem = FormatadorDeImagem.formataImagem(img, larguraJanela, alturaJanela);

            } catch (IOException ignored) { }

            lbl_foto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            lbl_foto.setSize(larguraJanela, alturaJanela);
            lbl_foto.setIcon(new ImageIcon(imagem));
        }
	 */
	/***************************************
          ATUALIZACAO DA BARRA DE STATUS
	 ***************************************/
	private void atualizaStatusBar(StatusBar sbar) {
		sbar.setMessage(getStatus());
	}

	private String getStatus() {

		String servidor = ConProperties;
		int n_registros = getListaDeAstronautas().getModel().getSize();

		return "SERVIDOR: \t" + servidor + "  REGISTROS: \t" + n_registros + " encontrados." +
		"  FILTROS: \t" + "[pais = " + getStrPais() + "] [sexo = " + getStrSexo() + "] [grupo = " + getStrGrupo() + "]" +
		"  ORDEM: \t" + Ordem.substring(1);

	}

	/*****************
    @class MenuHandler
	 *****************/
	public class MenuHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String acao = ((JMenuItem)e.getSource()).getText();

			if (acao.equals(sArquivo[1*3])) {
				JFileChooser dialogo = new JFileChooser();
				int resultado = dialogo.showOpenDialog(AstronautaGUI.this);
				if (resultado == JFileChooser.APPROVE_OPTION) {
					// TODO
					// File arqEscolhido = dialogo.getSelectedFile();
					// processamento do arquivo escolhido
					// taEditor.append("Arquivo: "+ arqEscolhido.getName() + "\n");
				}
			}

			if (acao.equals(sArquivo[7*3])) {System.exit(0);}

			// Banco de Dados - Exportar
			if (acao.equals(sBanco[0*3])){
				//TODO: implementar
				mostraMsgOperNaoImplementada();
			}

			// Banco de Dados - Importar
			if (acao.equals(sBanco[1*3])){
				//TODO: implementar
				mostraMsgOperNaoImplementada();
			}

			// Banco de Dados - Nuvem
			if (acao.equals(sBanco[2*3])){
				//TODO: implementar
				mostraMsgOperNaoImplementada();
			}

			// Banco de Dados - Rede
			if (acao.equals(sBanco[3*3])){
				//TODO: implementar
				mostraMsgOperNaoImplementada();
			}

			// Banco de Dados - EstatÃ­sticas
			if (acao.equals(sBanco[5*3])){
				//TODO: implementar
				mostraMsgOperNaoImplementada();
			}

			// Banco de Dados - Editar registro
			if (acao.equals(sBanco[7*3])){
				//TODO: implementar
				mostraMsgOperNaoImplementada();
			}

			// Banco de Dados - Inserir novo registro
			if (acao.equals(sBanco[8*3])){
				Astronauta astronauta = null;
				try {
					astronauta = new AstronautaCreate().getNovoAstronauta();
				} catch (HeadlessException | SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try (Connection con = ConnectionFactory.getConnection()){
					AstronautaDAO dao = new AstronautaDAO(con);
					dao.salva(astronauta);
				}	catch (SQLException evt){
					JOptionPane.showMessageDialog(null,
							"Nao foi possivel estabelecer conexao remota...",
							"Erro", JOptionPane.ERROR_MESSAGE, new ImageIcon("./imagens/vetor/scary.png") );
				}

				//mostraMsgOperNaoImplementada();

			}

			// Banco de Dados - Remover registro
			if (acao.equals(sBanco[9*3])){
				//TODO: implementar
				mostraMsgOperNaoImplementada();
			}

			/*******************************************************************
			 *                          FILTROS
			 ******************************************************************/
			// Filtro = missao
			if (acao.equals("MissÃ£o")) {
				String mission = ((String) JOptionPane.showInputDialog(
						AstronautaGUI.this,
						"Digite o nome da missao:",
						"Filtrar Resultados",
						JFrame.EXIT_ON_CLOSE,
						new ImageIcon("./imagens/vetor/space-rocket48.png"),
						null,
						null)).toLowerCase();

				DefaultListModel<Astronauta> modelAstroConsulta = new DefaultListModel<>();
				for (Astronauta astronauta : astronautas) {
					List<String> missoes = astronauta.getMissao();
					for (String missao : missoes) {

						if (missao.toLowerCase().contains(mission)) {
							modelAstroConsulta.addElement(astronauta);
							break;
						}

					}
				}

				jlistaDeAstronautas.setModel(modelAstroConsulta);
				jlistaDeAstronautas.setSelectedIndex(0);
				jlistaDeAstronautas.ensureIndexIsVisible(0);


			}

			// Filtro = parte do nome
			if (acao.equals("Parte do Nome")) {
				try {
					String parteDoNome = ((String) JOptionPane
							.showInputDialog(
									AstronautaGUI.this,
									"Digite parte do nome:",
									"Filtrar Resultados",
									JFrame.EXIT_ON_CLOSE,
									new ImageIcon(
											"./imagens/vetor/astronaut-icon.png"),
									null, null)).toLowerCase();
					DefaultListModel<Astronauta> modelAstroConsulta = new DefaultListModel<>();
					astronautas.stream().filter(astronauta -> astronauta.getPrimeiro_Nome().toLowerCase()
							.contains(parteDoNome)
							|| (astronauta.getNome_do_Meio()
									.toLowerCase()
									.contains(parteDoNome) || (astronauta
											.getSobrenome().toLowerCase()
											.contains(parteDoNome)))).forEach(modelAstroConsulta::addElement);
					jlistaDeAstronautas.setModel(modelAstroConsulta);
					jlistaDeAstronautas.setSelectedIndex(0);
					jlistaDeAstronautas.ensureIndexIsVisible(0);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

			if (acao.equals("Sobre ...")){

				mostraMsgSobre();
			}

			if (acao.equals("Atualiza")){
				System.out.println("atualizando ----------------------------------------->");
				jlistaDeAstronautas.filtra(getAstronautas(), getStrSexo(), getStrPais(), getStrGrupo());
				System.out.println("Operacao realizada com sucesso-------> " + getListaDeAstronautas().getModel().getSize() + " registros encontrados.");
				mostraStatusListaAstro();
			}

			if (acao.equals("Preparar Imagens")){

				// usar a rotina abaixo para preparar as imagens, no formato padrao 140px X 210px
				// Caso nao seja necessaria preparacao, comentar a linha de codigo!

				//***************************************************
				FormatadorDeImagem.preparaImagens(astronautas);
				//***************************************************
			}

			if (acao.equals("Testa Funcoes Especiais")){


				//**************************************************************************
				teste.testaFuncaoIsAlive.imprimeListaAstronautasFalecidos(astronautas);
				//**************************************************************************
			}

			// Ordenar por IdAstronauta = ordem de viagem ao espaÃ§o
			if (acao.equals(sOrdenar[0*3])) {
				//IdAstronautaComparator comparator = new IdAstronautaComparator();
				setOrdem(acao);
				Collections.sort(astronautas, OrdenarAstronautas.PorIdAstronauta.asc());
				jlistaDeAstronautas.filtra(getAstronautas(), getStrSexo(), getStrPais(), getStrGrupo());
			}

			// Ordenar por sobrenome
			if (acao.equals(sOrdenar[1*3])) {
				//SobrenomeComparator comparator = new SobrenomeComparator();
				setOrdem(acao);
				Collections.sort(astronautas, OrdenarAstronautas.PorSobrenome.asc());
				jlistaDeAstronautas.filtra(getAstronautas(), getStrSexo(), getStrPais(), getStrGrupo());
			}

			// Ordenar por data de nascimento
			if (acao.equals(sOrdenar[2*3])) {
				//DataNascComparator comparator = new DataNascComparator();
				setOrdem(acao);
				Collections.sort(astronautas, OrdenarAstronautas.PorDtNasc.asc());
				jlistaDeAstronautas.filtra(getAstronautas(), getStrSexo(), getStrPais(), getStrGrupo());
			}

			// Ordenar por numero de missoes
			if (acao.equals(sOrdenar[3*3])) {
				//MissoesComparator comparator = new MissoesComparator();
				setOrdem(acao);
				Collections.sort(astronautas, OrdenarAstronautas.PorNumDeMissoes.desc());
				jlistaDeAstronautas.filtra(getAstronautas(), getStrSexo(), getStrPais(), getStrGrupo());
			}

			// Ordenar por cidade de nascimento
			if (acao.equals(sOrdenar[4*3])) {
				//CidadeComparator comparator = new CidadeComparator();
				setOrdem(acao);
				Collections.sort(astronautas, OrdenarAstronautas.PorCidade.asc());
				jlistaDeAstronautas.filtra(getAstronautas(), getStrSexo(), getStrPais(), getStrGrupo());
			}

			// Ordenar por tempo no espaco
			if (acao.equals(sOrdenar[5*3])) {
				//TODO: implementar
				setOrdem(acao);
				mostraMsgOperNaoImplementada();
			}

			atualizaStatusBar(statusBar);
		}

		/**
		 * @throws HeadlessException
		 */
		public void mostraMsgSobre() throws HeadlessException {
			JOptionPane.showMessageDialog(
					AstronautaGUI.this,
					"(C)2015-2016 The Astronaut Database \n " + "http://www.astronautdatabase.com\n\n" +
							"Contact:\t Erasmo Leite Jr \n" +
							"\t erasmo@astronautdatabase.com \n\n" +
							"Thanks to:\n" +
							"- Spacefacts - http://www.spacefacts.de\n" +
							"- NASA - http://www.nasa.gov/\n" +
							"- ROSCOSMOS - http://www.federalspace.ru/\n" +
							"- ESA - http://www.esa.int/ESA\n" +
							"- ASTROnote - http://astronaut.ru/\n\n",
							"Viajantes Espaciais",
							JOptionPane.INFORMATION_MESSAGE,
							new ImageIcon("./imagens/vetor/Astronaut-50.png"));
		}

		/**
		 * @throws HeadlessException
		 */
		public void mostraMsgOperNaoImplementada() throws HeadlessException {
			JOptionPane.showMessageDialog(
					AstronautaGUI.this,
					"Huh... Nao entendi... Vou ficar te devendo essa...",
					"Alerta",
					JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon("./imagens/vetor/scary.png"));
		}

	}

	private class MenuPaisHandler implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent eventoPais) {

			if (eventoPais.getStateChange() == ItemEvent.SELECTED) {

				String paisSel = ((JMenuItem)eventoPais.getSource()).getIcon().toString();

				// A expressÃ£o abaixo retorna o cÃ³digo ISO-3 do paÃ­s, a partir do Ã­cone armazenado no JMenuItem
				setStrPais(paisSel.substring(16,19));

				atualizaFiltros();
			}

		}

	}


	private class MenuSexoHandler implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent eventoSexo) {

			if (eventoSexo.getStateChange() == ItemEvent.SELECTED) {

				String sexoSel = ((JMenuItem)eventoSexo.getSource()).getText();

				switch(sexoSel) {

				case "Ambos" : default	:	{setStrSexo("ALL"); 	break;}
				case "Homens" 			:	{setStrSexo("M");	 	break;}
				case "Mulheres" 		:	{setStrSexo("F");	 	break;}
				}

				atualizaFiltros();

			}
		}
	}


	private class MenuGrupoHandler implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent eventoTipo) {

			if (eventoTipo.getStateChange() == ItemEvent.SELECTED) {

				String grupo = ((JMenuItem)eventoTipo.getSource()).getText().substring(1);

				setStrGrupo(grupo.equals("Todos") ? "ALL": grupo);

				/*
				 * TODO: criar metodo para consulta
				 */
				atualizaFiltros();

			}
		}
	}



	/***************************************************
	 *           INICIALIZACAO DOS COMPONENTES
	 ***************************************************/

	private void inicializa() throws HeadlessException, SQLException {
		/*
		 * INICIALIZA DADOS DAS LISTAS E CRIA O PAINEL PRINCIPAL
		 */
		try {
			consultaSQL();  // realiza a consulta no banco de dados MySQL
			criaListas();   // cria JLists de astronautas e paises
			criaPainel();   // implementa a interface grafica
			criaMenu();     // cria os menus de Arquivo, Banco de Dados, Filtro.

		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					AstronautaGUI.this,
					"Nao foi possivel inicializar o sistema!",
					"Alerta",
					JOptionPane.ERROR_MESSAGE,
					new ImageIcon("./imagens/vetor/scary.png"));
			e.printStackTrace();
		}

	}


	/**
	 * @throws HeadlessException
	 */
	private static void mostraMsgIOException() throws HeadlessException {
		JOptionPane.showMessageDialog(
				null,
				"Alerta! Erro de I/O !",
				"Alerta",
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon("./imagens/vetor/scary.png"));
	}

	private static void mostraMsgSQLException() {
		JOptionPane.showMessageDialog(
				null,
				"Erro de Conexao!",
				"Alerta",
				JOptionPane.ERROR_MESSAGE);
	}

	private void mostraFiltros() {
		System.out.println("Filtro [PAIS = " + getStrPais() + "][SEXO = " + getStrSexo() + "][GRUPO = " + getStrGrupo() + "] -------> "
				+ getListaDeAstronautas().getModel().getSize() + " registros encontrados.");
	}

	/**
	 * 
	 */
	private void atualizaFiltros() {
		System.out.println("atualizando ----------------------------------------->");
		jlistaDeAstronautas.filtra(getAstronautas(), getStrSexo(), getStrPais(), getStrGrupo());
		mostraFiltros();
		atualizaStatusBar(statusBar);
		//mostraStatusListaAstro();
	}

	/****************************************************************************************
	 *                                    Metodo MAIN()
	 ****************************************************************************************/
	public static void main(String s[]) {
		SwingUtilities.invokeLater(() -> {
			try {
				new AstronautaGUI().setVisible(true);
			} catch (SQLException | IOException e) {
				mostraMsgSQLException();
				e.printStackTrace();
			}
		});
	}


}
