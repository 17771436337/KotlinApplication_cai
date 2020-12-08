package com.example.baselibrary.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * Created by 眼神
 * 类描述：处理压缩数据的工具
 */
object CompressUtils {
  private const val BUFFER_LENGTH = 400

  //压缩字节最小长度，小于这个长度的字节数组不适合压缩，压缩完会更大


  /**
   * 数据压缩
   *
   * @param ins
   * @param os
   * @throws Exception
   */
  @Throws(Exception::class)
  fun compress(ins: InputStream, os: OutputStream) {
    val gos = GZIPOutputStream(os)
    val data = ByteArray(BUFFER_LENGTH)
    val count = ins.read(data, 0, BUFFER_LENGTH)

    while (count != -1) {
      gos.write(data, 0, count)
    }
    gos.finish()

    gos.flush()
    gos.close()
  }

  /**
   * 数据解压缩
   *
   * @param ins
   * @param os
   * @throws Exception
   */
  @Throws(Exception::class)
  fun decompress(ins: InputStream, os: OutputStream) {
    val gis = GZIPInputStream(ins)
    val data = ByteArray(BUFFER_LENGTH)
    val count = gis.read(data, 0, BUFFER_LENGTH)
    while (count != -1) {
      os.write(data, 0, count)
    }

    gis.close()
  }

  /**
   * 数据解压缩
   *
   * @param ins
   * @throws Exception return 返回解析好的json字符串
   */
  @Throws(Exception::class)
  fun decompress(ins: InputStream): String {
    val gis = GZIPInputStream(ins)
    val os = ByteArrayOutputStream()
    val data = ByteArray(BUFFER_LENGTH)
    var count = gis.read(data, 0, BUFFER_LENGTH)
    while (count != -1) {
      os.write(data, 0, count)
      count = gis.read(data, 0, BUFFER_LENGTH)
    }

    os.close()
    gis.close()
    return String(os.toByteArray())
  }


  /**
   * 数据压缩
   *
   * @param data
   * @return
   * @throws Exception
   */
  @Throws(Exception::class)
  fun byteCompress(data: ByteArray): ByteArray {
    val bais = ByteArrayInputStream(data)
    val baos = ByteArrayOutputStream()
    // 压缩
    compress(bais, baos)
    val output = baos.toByteArray()
    baos.flush()
    baos.close()

    bais.close()

    return output
  }


  /**
   * 数据解压缩
   *
   * @param data
   * @return
   * @throws Exception
   */
  @Throws(Exception::class)
  fun byteDecompress(data: ByteArray): ByteArray {
    var data = data
    val bais = ByteArrayInputStream(data)
    val baos = ByteArrayOutputStream()

    // 解压缩

    decompress(bais, baos)

    data = baos.toByteArray()

    baos.flush()
    baos.close()

    bais.close()

    return data
  }


  /**
   * 压缩文件
   *
   * @param src
   * @param dest
   * @throws IOException
   */
  @Throws(IOException::class)
  fun zip(src: String, dest: String) {
    //提供了一个数据项压缩成一个ZIP归档输出流
    var out: ZipOutputStream? = null
    try {

      val outFile = File(dest)//源文件或者目录
      val fileOrDirectory = File(src)//压缩文件路径
      out = ZipOutputStream(FileOutputStream(outFile))
      //如果此文件是一个文件，否则为false。
      if (fileOrDirectory.isFile) {
        zipFileOrDirectory(out, fileOrDirectory, "")
      } else {
        //返回一个文件或空阵列。
        val entries = fileOrDirectory.listFiles()
        for (i in entries.indices) {
          // 递归压缩，更新curPaths
          zipFileOrDirectory(out, entries[i], "")
        }
      }
    } catch (ex: IOException) {
      ex.printStackTrace()
    } finally {
      //关闭输出流
      if (out != null) {
        try {
          out.close()
        } catch (ex: IOException) {
          ex.printStackTrace()
        }

      }
    }
  }

  /**
   * 压缩文件
   *
   * @param out
   * @param fileOrDirectory
   * @param curPath
   * @throws IOException
   */
  @Throws(IOException::class)
  private fun zipFileOrDirectory(
    out: ZipOutputStream,
    fileOrDirectory: File, curPath: String
  ) {
    //从文件中读取字节的输入流
    var ins: FileInputStream? = null
    try {
      //如果此文件是一个目录，否则返回false。
      if (!fileOrDirectory.isDirectory) {
        // 压缩文件
        val buffer = ByteArray(4096)
        var bytes_read = ins!!.read(buffer)
        ins = FileInputStream(fileOrDirectory)
        //实例代表一个条目内的ZIP归档
        val entry = ZipEntry(curPath + fileOrDirectory.name)
        //条目的信息写入底层流
        out.putNextEntry(entry)
        while ((bytes_read) != -1) {
          out.write(buffer, 0, bytes_read)
        }
        out.closeEntry()
      } else {
        // 压缩目录
        val entries = fileOrDirectory.listFiles()
        for (i in entries.indices) {
          // 递归压缩，更新curPaths
          zipFileOrDirectory(
            out, entries[i], curPath
                + fileOrDirectory.name + "/"
          )
        }
      }
    } catch (ex: IOException) {
      ex.printStackTrace()
      // throw ex;
    } finally {
      if (ins != null) {
        try {
          ins.close()
        } catch (ex: IOException) {
          ex.printStackTrace()
        }

      }
    }
  }


  /**
   * 解压
   *
   * @param zipFileName     压缩文件的文件名
   * @param outputDirectory 解压后的路径
   * @throws IOException
   */
  @Throws(IOException::class)
  fun unzip(zipFileName: String, outputDirectory: String) {
    var zipFile: ZipFile? = null
    try {
      zipFile = ZipFile(zipFileName)
      val e = zipFile.entries()
      var zipEntry: ZipEntry? = null
      val dest = File(outputDirectory)
      dest.mkdirs()
      while (e.hasMoreElements()) {
        zipEntry = e.nextElement() as ZipEntry
        val entryName = zipEntry.name
        var ins: InputStream? = null
        var out: FileOutputStream? = null
        try {
          if (zipEntry.isDirectory) {
            var name = zipEntry.name
            name = name.substring(0, name.length - 1)
            val f = File(
              outputDirectory + File.separator
                  + name
            )
            f.mkdirs()
          } else {
            var index = entryName.lastIndexOf("\\")
            if (index != -1) {
              val df = File(
                outputDirectory + File.separator
                    + entryName.substring(0, index)
              )
              df.mkdirs()
            }
            index = entryName.lastIndexOf("/")
            if (index != -1) {
              val df = File(
                outputDirectory + File.separator
                    + entryName.substring(0, index)
              )
              df.mkdirs()
            }
            val f = File(
              outputDirectory + File.separator
                  + zipEntry.name
            )
            // f.createNewFile();
            ins = zipFile.getInputStream(zipEntry)
            out = FileOutputStream(f)
            val by = ByteArray(1024)
            var c = ins!!.read(by)
            while (c != -1) {
              out.write(by, 0, c)
            }
            out.flush()
          }
        } catch (ex: IOException) {
          ex.printStackTrace()
          throw IOException("解压失败：$ex")
        } finally {
          if (ins != null) {
            try {
              ins.close()
            } catch (ex: IOException) {
            }

          }
          if (out != null) {
            try {
              out.close()
            } catch (ex: IOException) {
            }

          }
        }
      }
    } catch (ex: IOException) {
      ex.printStackTrace()
      throw IOException("解压失败：$ex")
    } finally {
      if (zipFile != null) {
        try {
          zipFile.close()
        } catch (ex: IOException) {
        }

      }
    }
  }
}
