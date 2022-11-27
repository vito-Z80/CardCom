-- phpMyAdmin SQL Dump
-- version 4.9.7
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Ноя 14 2022 г., 20:17
-- Версия сервера: 5.7.21-20-beget-5.7.21-20-1-log
-- Версия PHP: 5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `s99641nb_sk`
--

-- --------------------------------------------------------

--
-- Структура таблицы `nk_sk`
--
-- Создание: Июл 30 2022 г., 06:40
--

DROP TABLE IF EXISTS `nk_sk`;
CREATE TABLE `nk_sk` (
  `id` int(11) NOT NULL,
  `diameter` varchar(8) NOT NULL,
  `reel_number` varchar(20) NOT NULL,
  `reel_data` json NOT NULL,
  `last_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `nk_sk`
--

INSERT INTO `nk_sk` (`id`, `diameter`, `reel_number`, `reel_data`, `last_date`) VALUES
(1, '6.2', '145232', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(2, '23.5', '38221', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(3, '8.3', '545342', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(4, '6.2', '546245', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(5, '23.5', '35568456845', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(6, '6.2', '8974363', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(7, '6.9', '47454', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(8, '7.6', '3236623/2', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(9, '20', '346521963', '{\"cut\": 25, \"sent\": false, \"dt_cut\": \"12/12/22\", \"dt_sent\": \"12/13/22\", \"dt_work\": \"12/11/22\"}', '2022-07-30 06:40:40'),
(10, '22.5', '89910000034', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(11, '18.0', '58666666', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(12, '19.5', '34547', '{\"age\": 27, \"spouse\": null, \"address\": {\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10021-3100\", \"streetAddress\": \"21 2nd Street\"}, \"isAlive\": true, \"children\": [\"Catherine\", \"Thomas\", \"Trevor\"], \"lastName\": \"Smith\", \"firstName\": \"John\", \"phoneNumbers\": [{\"type\": \"home\", \"number\": \"212 555-1234\"}, {\"type\": \"office\", \"number\": \"646 555-4567\"}]}', '2022-07-30 06:40:40'),
(13, '', '100100200', '1000', '2022-07-30 06:40:40'),
(14, '', '100100200', '1000', '2022-07-30 06:40:40'),
(15, '', '100100200', '1000', '2022-07-30 06:40:40'),
(16, '', '100100200', '1000', '2022-07-30 06:40:40'),
(17, '', '100100200', '1000', '2022-07-30 06:40:40'),
(18, '', '100100200', '1000', '2022-07-30 06:40:40'),
(19, '', '100100200', '1000', '2022-07-30 06:40:40'),
(20, '20.5', '100100200', '{\"coming\": 650, \"number\": \"17748\", \"balance\": 650, \"diameter\": 4.5, \"segments\": [{\"length\": 112, \"sentDate\": \"\", \"workDate\": \"31-07-2022\", \"recipient\": \"Дядя петя Иванов\"}, {\"length\": 23, \"sentDate\": \"\", \"workDate\": \"31-07-2022\", \"recipient\": \"Дядя петя Иванов\"}, {\"length\": 4233, \"sentDate\": \"\", \"workDate\": \"31-07-2022\", \"recipient\": \"Дядя петя Иванов\"}, {\"length\": 12, \"sentDate\": \"\", \"workDate\": \"31-07-2022\", \"recipient\": \"Дядя петя Иванов\"}], \"comingDate\": \"31-07-2022 (07:54)\"}', '2022-07-31 18:15:59'),
(21, '11.5', '123654321456', '1000', '2022-07-30 06:40:40'),
(22, '12.0', '66766655', '640', '2022-07-30 06:40:40'),
(23, '17.5', '51066', '1000', '2022-07-30 06:40:40'),
(24, '6.2', '832423/2', '1000', '2022-07-30 06:41:29'),
(25, '3.6', '3623111', '520', '2022-07-30 06:48:16'),
(26, '6.2', '23344', '340', '2022-07-30 06:48:50'),
(27, '6.2', '89923', '245', '2022-07-30 07:11:15'),
(28, '6.2', '125723882221', '1000', '2022-07-30 07:20:25'),
(29, '6.2', '83275982375912312312', '2000', '2022-07-30 07:20:48'),
(30, '6.2', '718812', '1000', '2022-07-30 07:53:32'),
(31, '13.5', '1299033', '1000', '2022-07-30 09:34:28'),
(32, '13.5', '199923', '640', '2022-07-30 09:40:40'),
(33, '13.5', '238322', '{\"coming\": 750, \"number\": \"238322\", \"balance\": 750, \"diameter\": 13.5, \"segments\": [], \"comingDate\": \"\"}', '2022-07-30 09:50:19'),
(34, '7.6', '233312', '{\"coming\": 1000, \"number\": \"233312\", \"balance\": 1000, \"diameter\": 7.6, \"segments\": [], \"comingDate\": \"java.text.SimpleDateFormat@8629ad2d\"}', '2022-07-30 10:50:10'),
(35, '11.5', '234421', '{\"coming\": 1000, \"number\": \"234421\", \"balance\": 1000, \"diameter\": 11.5, \"segments\": [], \"comingDate\": \"java.text.SimpleDateFormat@9586200\"}', '2022-07-30 10:54:35'),
(36, '11.5', '355555', '{\"coming\": 1000, \"number\": \"355555\", \"balance\": 1000, \"diameter\": 11.5, \"segments\": [], \"comingDate\": \"30-07-2022\"}', '2022-07-30 10:56:30'),
(37, '8.3', '11232', '{\"coming\": 1000, \"number\": \"11232\", \"balance\": 1000, \"diameter\": 8.3, \"segments\": [], \"comingDate\": \"30-07-2022 (10:58:14)\"}', '2022-07-30 10:58:36'),
(38, '3.6', '100241', '{\"coming\": 1000, \"number\": \"100241\", \"balance\": 1000, \"diameter\": 3.6, \"segments\": [], \"comingDate\": \"30-07-2022 (10:59)\"}', '2022-07-30 10:59:20'),
(39, '4.5', '16625', '{\"coming\": 1000, \"number\": \"16625\", \"balance\": 1000, \"diameter\": 4.5, \"segments\": [], \"comingDate\": \"31-07-2022 (07:52)\"}', '2022-07-31 07:54:33'),
(40, '4.5', '17748', '{\"coming\": 650, \"number\": \"17748\", \"balance\": 650, \"diameter\": 4.5, \"segments\": [], \"comingDate\": \"31-07-2022 (07:54)\"}', '2022-07-31 07:55:34'),
(41, '88.0', '88888888', '{\"coming\": 99, \"number\": \"88888888\", \"balance\": 99, \"diameter\": 88, \"segments\": [], \"comingDate\": \"03-08-2022 (14:47)\"}', '2022-08-03 14:48:12'),
(42, '99.0', '999999', '{\"coming\": 1000, \"number\": \"999999\", \"balance\": 1000, \"diameter\": 99, \"segments\": [], \"comingDate\": \"03-08-2022 (14:48)\"}', '2022-08-03 14:48:48');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `nk_sk`
--
ALTER TABLE `nk_sk`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `nk_sk`
--
ALTER TABLE `nk_sk`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
