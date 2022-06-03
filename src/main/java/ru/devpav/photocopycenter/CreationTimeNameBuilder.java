package ru.devpav.photocopycenter;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL;

public class CreationTimeNameBuilder implements FileNameBuilder {

    public CreationTimeNameBuilder() {}

    @Override
    public String buildName(Path path) {
        return getFileCreationDate(path);
    }
    

    private String getFileCreationDate(Path path) {
        final ImageMetadata metadata;
        String valueTag = null;
        try {
            metadata = Imaging.getMetadata(path.toFile());

            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

            valueTag = getOriginalTime(jpegMetadata, EXIF_TAG_DATE_TIME_ORIGINAL);
        } catch (Exception ignored) {
        }


        return getFolderName(valueTag);
    }

    private String getFolderName(String date) {
        final DateFormat df = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
        final SimpleDateFormat format = new SimpleDateFormat("yyyy dd MM");

        try {
            final String substring = date.substring(1, date.length() - 1);
            final Date parseDate = df.parse(substring);
            return format.format(parseDate).trim();
        } catch (Exception ex) {
            return format.format(new Date()).trim();
        }
    }

    private String getOriginalTime(final JpegImageMetadata jpegMetadata, final TagInfo tagInfo) {
        final TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
        return field == null ? "undefined" : field.getValueDescription();
    }

}
