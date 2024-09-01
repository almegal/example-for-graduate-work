<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>

    <xsl:template match="/">
        <html>
            <body>
                <h2>Checkstyle Report</h2>
                <table border="1">
                    <tr>
                        <th>File</th>
                        <th>Line</th>
                        <th>Message</th>
                    </tr>
                    <xsl:for-each select="checkstyle/file">
                        <xsl:for-each select="error">
                            <tr>
                                <td><xsl:value-of select="@name"/></td>
                                <td><xsl:value-of select="@line"/></td>
                                <td><xsl:value-of select="@message"/></td>
                            </tr>
                        </xsl:for-each>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
