package org.docx4j.openpackaging.parts.WordprocessingML;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class Office2010WordprocessingDrawingTest {

  /**
   * A document containing an image with option wrap text -> in front of text cannot be processed
   * because the name space <em>http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing</em> is
   * unknown to docx4j.
   */
  @Test
  public void testImageWrapTextInFrontOfText() throws Exception {
    String minimalXml = "" +
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
        "<w:document\n" +
        "  xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\"\n" +
        "  xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\"\n" +
        "  xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" +
        "  xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"\n" +
        "  xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"\n" +
        "  xmlns:v=\"urn:schemas-microsoft-com:vml\"\n" +
        "  xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\"\n" +
        "  xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\"\n" +
        "  xmlns:w10=\"urn:schemas-microsoft-com:office:word\"\n" +
        "  xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"\n" +
        "  xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\"\n" +
        "  xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\"\n" +
        "  xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\"\n" +
        "  xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\"\n" +
        "  xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\"\n" +
        "  mc:Ignorable=\"w14 wp14\">\n" +
        "  <w:body>\n" +
        "    <w:p>\n" +
        "      <w:r>\n" +
        "        <w:rPr />\n" +
        "        <w:drawing>\n" +
        "          <wp:anchor>\n" +
        "            <wp14:sizeRelH />\n" +
        "          </wp:anchor>\n" +
        "        </w:drawing>\n" +
        "      </w:r>\n" +
        "    </w:p>\n" +
        "  </w:body>\n" +
        "</w:document>";
    MainDocumentPart main = new MainDocumentPart();
    main.unmarshal(new ByteArrayInputStream(minimalXml.getBytes()));
    assertNotNull(main.getJaxbElement());
    assertNotNull(main.getBinder().getXMLNode(main.getJaxbElement()));
  }
}
