package com.tecnicasilegais.huffman;

import manifold.ext.rt.api.Jailbreak;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


class HuffmanEncodingTest {
    @Jailbreak HuffmanEncoding he = null;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        Path input = tempDir.resolve("in.txt");
        Files.writeString(input, "AAAABBBCCCCCC \r\n");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void start() {

    }
}