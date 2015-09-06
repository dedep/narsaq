package dedep.narsaq.photo.concat;

import dedep.narsaq.PropertiesService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PhotoConcatenerImplTest {

    @InjectMocks
    private PhotoConcatenerImpl photoConcatener;

    @Mock
    private PropertiesService propertiesService;

    @Test
    public void shouldAddWhiteSpaceAtBottom() {
        Integer w = 3840;
        Integer h = 5120;

        when(propertiesService.getDouble(PhotoConcatenerImpl.PHOTO_RATIO)).thenReturn(0.666666);
        PhotoConcatenerImpl.Size result = photoConcatener.calculateSize(w, h);

        Assert.assertEquals(result.getWidth(), 3840);
        Assert.assertEquals(result.getHeight(), 5760);
    }

    @Test
    public void shouldAddWhiteSpaceAtRight() {
        Integer w = 3840;
        Integer h = 6120;

        when(propertiesService.getDouble(PhotoConcatenerImpl.PHOTO_RATIO)).thenReturn(0.666666);
        PhotoConcatenerImpl.Size result = photoConcatener.calculateSize(w, h);

        Assert.assertEquals(result.getWidth(), 4079);
        Assert.assertEquals(result.getHeight(), 6120);
    }

}
