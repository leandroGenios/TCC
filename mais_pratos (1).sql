-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 09-Ago-2019 às 22:46
-- Versão do servidor: 10.1.36-MariaDB
-- versão do PHP: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mais_pratos`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `classificacao`
--

CREATE TABLE `classificacao` (
  `id` int(11) NOT NULL,
  `descricao` varchar(45) NOT NULL,
  `menor_valor` int(11) NOT NULL,
  `maior_valor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `classificacao`
--

INSERT INTO `classificacao` (`id`, `descricao`, `menor_valor`, `maior_valor`) VALUES
(1, 'INICIANTE', 0, 5),
(2, 'nivel 2', 6, 10);

-- --------------------------------------------------------

--
-- Estrutura da tabela `comentario`
--

CREATE TABLE `comentario` (
  `id` int(11) NOT NULL,
  `prato_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `descricao` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `ingrediente`
--

CREATE TABLE `ingrediente` (
  `id` int(11) NOT NULL,
  `codigo_barras` double DEFAULT NULL,
  `nome` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `ingrediente`
--

INSERT INTO `ingrediente` (`id`, `codigo_barras`, `nome`) VALUES
(1, 7891098037503, 'CHÁ BRANCO'),
(2, 7891098037500, 'CHÁ VERDE'),
(3, 7891098037000, 'CHÁ MATE'),
(5, NULL, 'MACARRAO'),
(7, NULL, 'ALHO'),
(8, NULL, 'ÓLEO'),
(9, NULL, 'CENOURA'),
(10, NULL, 'AÇÚCAR'),
(11, NULL, 'FARINHA DE TRIGO'),
(12, NULL, 'FERMENTO QUÍMICO'),
(13, NULL, 'SAL'),
(14, NULL, 'AGUA'),
(15, NULL, 'CEBOLINHA'),
(16, NULL, 'ESPAGUETE'),
(17, NULL, 'BACON'),
(18, NULL, 'OVO'),
(19, NULL, 'CREME DE LEITE'),
(20, NULL, 'QUEIJO PARMES?O');

-- --------------------------------------------------------

--
-- Estrutura da tabela `lista_compra`
--

CREATE TABLE `lista_compra` (
  `usuario_id` int(11) NOT NULL,
  `ingrediente_id` int(11) NOT NULL,
  `unidade_medida_id` int(11) NOT NULL,
  `quantidade` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `lista_compra`
--

INSERT INTO `lista_compra` (`usuario_id`, `ingrediente_id`, `unidade_medida_id`, `quantidade`) VALUES
(1, 5, 1, 1000),
(1, 18, 8, 4);

-- --------------------------------------------------------

--
-- Estrutura da tabela `prato`
--

CREATE TABLE `prato` (
  `id` int(11) NOT NULL,
  `nome` varchar(150) NOT NULL,
  `modo_preparo` varchar(5000) NOT NULL,
  `tempo_preparo` int(11) NOT NULL,
  `imagem` blob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `prato`
--

INSERT INTO `prato` (`id`, `nome`, `modo_preparo`, `tempo_preparo`, `imagem`) VALUES
(7, 'Bolo de cenoura de liquidificadordx', 'Preaquece o forno a 180C.\nUnte com óleo uma forma retangular de aproximadamente 30cm x 25cm, polvilhe com a farinha de trigo e bata a forma para remover bem o excesso.\nEm uma tigela grande misture a farinha, o fermento, o sal e reserve.\nDescasque as cenouras, corte em rodelas e coloque no liquidificador, adicione o óleo, o açúcar e os ovos e bata até obter um creme liso.\nAos poucos comece adicionar os líquidos aos ingredientes secos, use um fouet para misturar os ingredientes até obter uma massa lisa e homogênea.\nTransfira a massa para a forma preparada e leve para assar por aproximadamente 30 minutos.\nDepois desse tempo, espete um palito no centro da massa. Se o palito sair limpo é hora de retirar do forno, se sair com um pouco de massa coloque o bolo de volta no forno até assar completamente.\nCorte o bolo ainda quente sem retirá-lo da forma.', 1, NULL),
(8, 'Macarrao alho e óleo', 'Leve a água para ferver, quando começar a ferver coloque o sal e a massa.\nVerifique na embalagem o tempo de cozimento para al dente da massa escolhida, pois cada massa tem um tempo de cozimento. E sempre use 1 l de água para cada 100 g de massa, isto fará com que ela cozinhe por igual e não grude.\nAqueça o óleo em uma frigideira grande. Frite o alho em fogo bem baixo até que comece a dourar.\nJunte a massa já cozida e escorrida, misture bem. Coloque a cebolinha e esta pronto.', 1, NULL),
(9, 'Espaguete à carbonara', 'Essa é uma receita muito simples e rápida, que você pode dividir em duas etapas. Enquanto cozinha a massa, seguindo as instruções do pacotinho (geralmente entre 6 e 8 minutos cozinhando em água fervente), vamos fritar o bacon. Usei bacon em tiras e cortei picadinho. Frite em uma frigideira mas não deixe queimar. Reserve.\n\n2. Quando o macarrão estiver pronto, escorra bem a água mas não lave! Volte com a massa para o fogo baixo e acrescentaremos o bacon + mistura de ovos ligeiramente batidos, temperado com o queijo e pimenta do reino. O ovo é cru mesmo – fique tranquilo que o calor da massa vai cozinhá-lo.\n\n3. Por fim, acrescente o creme de leite e misture bem.\n\n*A receita original de Carbonara não leva creme de leite. O ingrediente foi adicionado para aumentar a cremosidade do molho (uma adaptação norte-americana da receita).\n\n4. Acerte o sal se necessário mas não exagere pois o bacon e o queijo já são bem salgadinhos.', 1, NULL),
(10, 'GFDGD', 'FSFSDFDS', 20, NULL);

-- --------------------------------------------------------

--
-- Estrutura da tabela `prato_avaliacao`
--

CREATE TABLE `prato_avaliacao` (
  `prato_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `avaliacao` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `prato_avaliacao`
--

INSERT INTO `prato_avaliacao` (`prato_id`, `usuario_id`, `avaliacao`) VALUES
(7, 1, 2),
(10, 1, 4);

-- --------------------------------------------------------

--
-- Estrutura da tabela `prato_favorito`
--

CREATE TABLE `prato_favorito` (
  `prato_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `favorito` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `prato_ingrediente`
--

CREATE TABLE `prato_ingrediente` (
  `prato_id` int(11) NOT NULL,
  `ingrediente_id` int(11) NOT NULL,
  `unidade_medida_id` int(11) NOT NULL,
  `quantidade` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `prato_ingrediente`
--

INSERT INTO `prato_ingrediente` (`prato_id`, `ingrediente_id`, `unidade_medida_id`, `quantidade`) VALUES
(7, 8, 1, 360),
(7, 9, 1, 370),
(7, 10, 1, 360),
(7, 11, 1, 390),
(7, 12, 1, 18),
(7, 13, 1, 5),
(10, 5, 1, 500),
(10, 18, 8, 2);

-- --------------------------------------------------------

--
-- Estrutura da tabela `prato_preparo`
--

CREATE TABLE `prato_preparo` (
  `prato_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `inicio_preparo` bigint(11) NOT NULL,
  `preparo_sem_ingredientes` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `prato_preparo`
--

INSERT INTO `prato_preparo` (`prato_id`, `usuario_id`, `inicio_preparo`, `preparo_sem_ingredientes`) VALUES
(10, 1, 1563822889090, 1),
(7, 1, 1564062416016, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `prato_usuario`
--

CREATE TABLE `prato_usuario` (
  `prato_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `prato_usuario`
--

INSERT INTO `prato_usuario` (`prato_id`, `usuario_id`) VALUES
(7, 1),
(8, 1),
(9, 1),
(10, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `unidade_medida`
--

CREATE TABLE `unidade_medida` (
  `id` int(11) NOT NULL,
  `sigla` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `unidade_medida`
--

INSERT INTO `unidade_medida` (`id`, `sigla`) VALUES
(1, 'gramas'),
(2, 'mililitros'),
(8, 'unidade');

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `email` varchar(150) NOT NULL,
  `senha` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `usuario`
--

INSERT INTO `usuario` (`id`, `nome`, `email`, `senha`) VALUES
(1, 'Leandro Soares', 'leandro.genios@gmail.com', '123'),
(2, 'Ketlin', 'teste@teste.com.br', 'teste'),
(3, 'Carlos', 'teste@teste', '123');

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario_amigo`
--

CREATE TABLE `usuario_amigo` (
  `usuario_id` int(11) NOT NULL,
  `usuario_amigo_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `usuario_amigo`
--

INSERT INTO `usuario_amigo` (`usuario_id`, `usuario_amigo_id`) VALUES
(1, 2);

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario_classificacao`
--

CREATE TABLE `usuario_classificacao` (
  `usuario_id` int(11) NOT NULL,
  `classificacao_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `usuario_classificacao`
--

INSERT INTO `usuario_classificacao` (`usuario_id`, `classificacao_id`) VALUES
(1, 1),
(2, 1),
(3, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario_ingrediente`
--

CREATE TABLE `usuario_ingrediente` (
  `usuario_id` int(11) NOT NULL,
  `ingrediente_id` int(11) NOT NULL,
  `unidade_medida_id` int(11) NOT NULL,
  `quantidade` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `usuario_ingrediente`
--

INSERT INTO `usuario_ingrediente` (`usuario_id`, `ingrediente_id`, `unidade_medida_id`, `quantidade`) VALUES
(1, 20, 1, 150);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `classificacao`
--
ALTER TABLE `classificacao`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `comentario`
--
ALTER TABLE `comentario`
  ADD PRIMARY KEY (`id`),
  ADD KEY `prato_id` (`prato_id`),
  ADD KEY `usuario_id` (`usuario_id`);

--
-- Indexes for table `ingrediente`
--
ALTER TABLE `ingrediente`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `lista_compra`
--
ALTER TABLE `lista_compra`
  ADD PRIMARY KEY (`usuario_id`,`ingrediente_id`),
  ADD KEY `ingrediente_id` (`ingrediente_id`),
  ADD KEY `unidade_medida_id` (`unidade_medida_id`);

--
-- Indexes for table `prato`
--
ALTER TABLE `prato`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `prato_avaliacao`
--
ALTER TABLE `prato_avaliacao`
  ADD PRIMARY KEY (`prato_id`,`usuario_id`),
  ADD KEY `usuario_id` (`usuario_id`);

--
-- Indexes for table `prato_favorito`
--
ALTER TABLE `prato_favorito`
  ADD PRIMARY KEY (`prato_id`,`usuario_id`),
  ADD KEY `usuario_id` (`usuario_id`);

--
-- Indexes for table `prato_ingrediente`
--
ALTER TABLE `prato_ingrediente`
  ADD PRIMARY KEY (`prato_id`,`ingrediente_id`),
  ADD KEY `ingrediente_id` (`ingrediente_id`),
  ADD KEY `unidade_medida_id` (`unidade_medida_id`);

--
-- Indexes for table `prato_preparo`
--
ALTER TABLE `prato_preparo`
  ADD KEY `prato_id` (`prato_id`),
  ADD KEY `usuario_id` (`usuario_id`);

--
-- Indexes for table `prato_usuario`
--
ALTER TABLE `prato_usuario`
  ADD PRIMARY KEY (`prato_id`,`usuario_id`);

--
-- Indexes for table `unidade_medida`
--
ALTER TABLE `unidade_medida`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `usuario_amigo`
--
ALTER TABLE `usuario_amigo`
  ADD PRIMARY KEY (`usuario_id`,`usuario_amigo_id`),
  ADD KEY `usuario_amigo_id` (`usuario_amigo_id`);

--
-- Indexes for table `usuario_classificacao`
--
ALTER TABLE `usuario_classificacao`
  ADD PRIMARY KEY (`usuario_id`,`classificacao_id`),
  ADD KEY `classificacao_id` (`classificacao_id`);

--
-- Indexes for table `usuario_ingrediente`
--
ALTER TABLE `usuario_ingrediente`
  ADD PRIMARY KEY (`usuario_id`,`ingrediente_id`),
  ADD KEY `ingrediente_id` (`ingrediente_id`),
  ADD KEY `unidade_medida_id` (`unidade_medida_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `classificacao`
--
ALTER TABLE `classificacao`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `comentario`
--
ALTER TABLE `comentario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ingrediente`
--
ALTER TABLE `ingrediente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `prato`
--
ALTER TABLE `prato`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `unidade_medida`
--
ALTER TABLE `unidade_medida`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `comentario`
--
ALTER TABLE `comentario`
  ADD CONSTRAINT `comentario_ibfk_1` FOREIGN KEY (`prato_id`) REFERENCES `prato` (`id`),
  ADD CONSTRAINT `comentario_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Limitadores para a tabela `lista_compra`
--
ALTER TABLE `lista_compra`
  ADD CONSTRAINT `lista_compra_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `lista_compra_ibfk_2` FOREIGN KEY (`ingrediente_id`) REFERENCES `ingrediente` (`id`),
  ADD CONSTRAINT `lista_compra_ibfk_3` FOREIGN KEY (`unidade_medida_id`) REFERENCES `unidade_medida` (`id`);

--
-- Limitadores para a tabela `prato_avaliacao`
--
ALTER TABLE `prato_avaliacao`
  ADD CONSTRAINT `prato_avaliacao_ibfk_1` FOREIGN KEY (`prato_id`) REFERENCES `prato` (`id`),
  ADD CONSTRAINT `prato_avaliacao_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Limitadores para a tabela `prato_favorito`
--
ALTER TABLE `prato_favorito`
  ADD CONSTRAINT `prato_favorito_ibfk_1` FOREIGN KEY (`prato_id`) REFERENCES `prato` (`id`),
  ADD CONSTRAINT `prato_favorito_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Limitadores para a tabela `prato_ingrediente`
--
ALTER TABLE `prato_ingrediente`
  ADD CONSTRAINT `prato_ingrediente_ibfk_1` FOREIGN KEY (`prato_id`) REFERENCES `prato` (`id`),
  ADD CONSTRAINT `prato_ingrediente_ibfk_2` FOREIGN KEY (`ingrediente_id`) REFERENCES `ingrediente` (`id`),
  ADD CONSTRAINT `prato_ingrediente_ibfk_3` FOREIGN KEY (`unidade_medida_id`) REFERENCES `unidade_medida` (`id`);

--
-- Limitadores para a tabela `prato_preparo`
--
ALTER TABLE `prato_preparo`
  ADD CONSTRAINT `prato_preparo_ibfk_1` FOREIGN KEY (`prato_id`) REFERENCES `prato` (`id`),
  ADD CONSTRAINT `prato_preparo_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Limitadores para a tabela `usuario_amigo`
--
ALTER TABLE `usuario_amigo`
  ADD CONSTRAINT `usuario_amigo_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `usuario_amigo_ibfk_2` FOREIGN KEY (`usuario_amigo_id`) REFERENCES `usuario` (`id`);

--
-- Limitadores para a tabela `usuario_classificacao`
--
ALTER TABLE `usuario_classificacao`
  ADD CONSTRAINT `usuario_classificacao_ibfk_1` FOREIGN KEY (`classificacao_id`) REFERENCES `classificacao` (`id`),
  ADD CONSTRAINT `usuario_classificacao_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Limitadores para a tabela `usuario_ingrediente`
--
ALTER TABLE `usuario_ingrediente`
  ADD CONSTRAINT `usuario_ingrediente_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `usuario_ingrediente_ibfk_2` FOREIGN KEY (`ingrediente_id`) REFERENCES `ingrediente` (`id`),
  ADD CONSTRAINT `usuario_ingrediente_ibfk_3` FOREIGN KEY (`unidade_medida_id`) REFERENCES `unidade_medida` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
