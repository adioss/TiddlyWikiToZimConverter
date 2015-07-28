package com.adioss.tiddlyzim;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class ConverterTest {

    @Test
    public void shouldExtractNullLinkListIfContentIsEmpty() throws Exception {
        // Given
        Converter converter = new Converter();
        // When
        List<String> links = converter.extractLinks("");
        // Then
        assertThat(links).isNull();
    }

    @Test
    public void shouldExtractSimpleLink() throws Exception {
        // Given
        List<String> expected = singletonList("test");
        Converter converter = new Converter();
        // When
        List<String> links = converter.extractLinks("[[test]]");
        // Then
        assertThat(links).hasSameSizeAs(expected).containsAll(expected);
    }

    @Test
    public void shouldExtractSimpleLinkWithDescription() throws Exception {
        // Given
        List<String> expected = singletonList("Link");
        Converter converter = new Converter();
        // When
        List<String> links = converter.extractLinks("[[description|Link]]");
        // Then
        assertThat(links).hasSameSizeAs(expected).containsAll(expected);
    }

    @Test
    public void shouldExtractMultipleLinks() throws Exception {
        // Given
        List<String> expected = asList("Link", "Link2", "Link3");
        Converter converter = new Converter();
        // When
        List<String> links = converter.extractLinks("[[description|Link]]\n[[Link2]][[description|Link3]]");
        // Then
        assertThat(links).hasSameSizeAs(expected).containsAll(expected);
    }
}