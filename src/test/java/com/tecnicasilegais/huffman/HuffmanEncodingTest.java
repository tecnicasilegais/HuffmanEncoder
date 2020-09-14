package com.tecnicasilegais.huffman;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

class HuffmanEncodingTest {
    @Test
    void start() {
        HashMap<Character, Integer> map = HuffmanEncoding.StringToFrequencyMap("AAAABBBCCCCCC \r\n");
        assertEquals(4, (int) map.get('A'), "Contagem de 'A's errada.");
        assertEquals(3, (int) map.get('B'), "Contagem de 'B's errada.");
        assertEquals(6, (int) map.get('C'), "Contagem de 'C's errada.");
        assertEquals(1, (int) map.get(' '), "Contagem de espaços errada.");
        assertEquals(1, (int) map.get('\r'), "Contagem de carriage return errada.");
        assertEquals(1, (int) map.get('\n'), "Contagem de newline errada.");
    }
}