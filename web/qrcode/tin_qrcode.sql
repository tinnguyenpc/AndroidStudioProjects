-- phpMyAdmin SQL Dump
-- version 4.7.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost
-- Thời gian đã tạo: Th9 22, 2018 lúc 02:26 PM
-- Phiên bản máy phục vụ: 5.6.34
-- Phiên bản PHP: 5.5.38

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `tin_qrcode`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tb_data`
--

CREATE TABLE `tb_data` (
  `id` int(11) NOT NULL,
  `session_app` datetime NOT NULL,
  `qr_code` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `url_img` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `person` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `heath` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time_out` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tb_qrcode`
--

CREATE TABLE `tb_qrcode` (
  `id` int(11) NOT NULL,
  `code` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `session_qr` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `tb_qrcode`
--

INSERT INTO `tb_qrcode` (`id`, `code`, `name`, `session_qr`) VALUES
(1, 'grsc_0001', '01', NULL),
(2, 'grsc_0002', '02', NULL),
(3, 'grsc_0003', '03', NULL),
(4, 'grsc_0004', '04', NULL),
(5, 'grsc_0005', '05', NULL),
(6, 'grsc_0006', '06', NULL),
(7, 'grsc_0007', '07', NULL),
(8, 'grsc_0008', '08', NULL),
(9, 'grsc_0009', '09', NULL),
(10, 'grsc_0010', '10', NULL),
(11, 'grsc_0011', '11', NULL),
(12, 'grsc_0012', '12', NULL),
(13, 'grsc_0013', '13', NULL),
(14, 'grsc_0014', '14', NULL),
(15, 'grsc_0015', '15', NULL),
(16, 'grsc_0016', '16', NULL),
(17, 'grsc_0017', '17', NULL),
(18, 'grsc_0018', '18', NULL),
(19, 'grsc_0019', '19', NULL),
(20, 'grsc_0020', '20', NULL),
(21, 'grsc_0021', '21', NULL),
(22, 'grsc_0022', '22', NULL),
(23, 'grsc_0023', '23', NULL),
(24, 'grsc_0024', '24', NULL),
(25, 'grsc_0025', '25', NULL),
(26, 'grsc_0026', '26', NULL),
(27, 'grsc_0027', '27', NULL),
(28, 'grsc_0028', '28', NULL),
(29, 'grsc_0029', '29', NULL),
(30, 'grsc_0030', '30', NULL);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `tb_data`
--
ALTER TABLE `tb_data`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `tb_qrcode`
--
ALTER TABLE `tb_qrcode`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `tb_data`
--
ALTER TABLE `tb_data`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT cho bảng `tb_qrcode`
--
ALTER TABLE `tb_qrcode`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
