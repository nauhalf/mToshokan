package com.dicoding.naufal.mtoshokan.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    var bookId: Long?,
    var bookTitle: String?,
    var bookWriter: String?,
    var bookCover: String?,
    var bookSynopsis: String?,
    var bookPublisher: String?,
    var bookType: BookType?,
    var bookLocation: String?,
    var bookAvaibility: Boolean?
) : Parcelable

val bookHyouka = Book(
    1,
    "Hyouka",
    "Yonezawa Honobu",
    "hyouka.jpg",
    "Kalau kita menyelidikinya, mungkin akan terjadi hal-hal yang tidak baik. Oreki Hotaro adalah pemuda hemat energi. Mottonya adalah, “Kalau tidak perlu " +
            "dikerjakan, lebih baik tidak usah dikerjakan. Tapi kalau harus dikerjakan, lakukan dengan praktis.” Hanya saja, semua itu berubah saat dia terpaksa bergabung dengan " +
            "Klub Sastra Klasik. Chitanda Eru—gadis dengan rasa " +
            "penasaran yang tinggi—mengubah hari-hari Hotaro, dan dia harus memecahkan misteri demi misteri yang terjadi di sekitar mereka. Gara-gara Chitanda, mereka dihadapkan pada kasus 33 tahun yang lalu. Hanya saja, " +
            "petunjuk mereka hanyalah sebuah antologi berjudul Hyouka.",
    "Penerbit Haru",
    bookTypeNovel,
    "F5-9",
    true
)

val bookCreditRoll = Book(
    2,
    "Hyouka 2 : Credit Roll of The Fool",
    "Yonezawa Honobu",
    "creditfool.jpg",
    "Oreki Hotaro lagi-lagi terseret oleh rasa ingin tahu Chitanda Eru. Melawan " +
            "keinginannya, kali ini Hotaro harus menebak penyelesaian skenario naskah film misteri " +
            "kelas 2-F yang akan ditayangkan saat Festival Kanya nanti. " +
            "Seorang siswa terjebak dalam kamar tertutup bangunan terbengkelai, mati setelah " +
            "tangannya terpotong. Namun, siapa yang membunuh? Bagaimana caranya? Film itu " +
            "selesai begitu saja tanpa penjelasan. Hotaro-lah yang bertugas untuk menebak siapa ada " +
            "bagaimana trik pembunuhan itu dilakukan. " +
            "Namun, hanya itukah masalahnya? " +
            "Atau ada sesuatu yang lebih besar dari sekedar menyelesaikan skenario film?",
    "Penerbit Haru",
    bookTypeNovel,
    "F5-10",
    true
)

val bookKudryavka = Book(
    3,
    "Hyouka 3 : Kudryavka Sequence",
    "Yonezawa Honobu",
    "creditfool.jpg",
    "Akhirnya Festifal Budaya yang dinantikan pun tiba. Namun, masalah besar terjadi dalam Klub Sastra Klasik. Gara-gara sebuah kesalahan, antologi Hyouka dicetak terlalu banyak. " +
            "Saat semua orang kebingungan memikirkan cara untuk menjual habis antologi itu, sebuah kasus pencurian yang aneh pun terjadi di dalam kompleks sekolah. " +
            "Bisakan mereka menjual habis antologi yang kebanyakan itu di tengah riuhnya festival? Apakah Hotaro akan menyelesaikan kasus pencurian kali ini?",
    "Penerbit Haru",
    bookTypeNovel,
    "F5-11",
    true
)

val bookOnajiYume = Book(
    4,
    "I Saw The Same Dream Again",
    "Sumino Yoru",
    "mataonajiyume.jpg",
    "Koyanagi Nanoka adalah seorang pelajar SD yang menganggap dirinya sendiri pintar. " +
            "Dia mendapatkan tugas sekolah untuk memikirkan apa itu kebahagiaan. " +
            "Selama memikirkan tugas tersebut bersama dengan teman sekelasnya, dia bertemu dengan " +
            "pelajar SMA yang suka menyayat nadinya, seorang wanita yang terjebak dalam hidupnya " +
            "sendiri, dan seorang nenek yang tampaknya hidup damai. Semuanya memiliki penyesalan " +
            "masing-masing.\n" +
            "Apakah kebahagiaan itu?\n" +
            "Bisakah mereka memperbaiki masa lalu?",
    "Penerbit Haru",
    bookTypeNovel,
    "F5-11",
    true
)

