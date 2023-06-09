package framework.telegram.ui.filepicker.config

import framework.telegram.ui.filepicker.bean.FileItemBeanImpl
import framework.telegram.ui.filepicker.filetype.*

/**
 *
 * @author rosu
 * @date 2018/11/27
 */
class DefaultFileType: AbstractFileType(){

    private val allDefaultFileType:ArrayList<FileType> by lazy {
        val fileTypes = ArrayList<FileType>()
        fileTypes.add(AudioFileType())
        fileTypes.add(RasterImageFileType())
        fileTypes.add(CompressedFileType())
        fileTypes.add(DataBaseFileType())
        fileTypes.add(ExecutableFileType())
        fileTypes.add(FontFileType())
        fileTypes.add(PageLayoutFileType())
        fileTypes.add(TextFileType())
        fileTypes.add(WordFileType())
        fileTypes.add(ExcelFileType())
        fileTypes.add(VideoFileType())
        fileTypes.add(WebFileType())
        fileTypes.add(PowerPointFileType())
        fileTypes
    }

    override fun fillFileType(itemBeanImpl: FileItemBeanImpl): FileItemBeanImpl {
        for (type in allDefaultFileType){
            if (type.verify(itemBeanImpl.fileName.toLowerCase())){
                itemBeanImpl.fileType = type
                break
            }
        }
        return itemBeanImpl
    }
}