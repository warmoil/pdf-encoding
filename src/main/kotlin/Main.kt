import model.PWModel
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.encryption.AccessPermission
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy
import java.io.File


fun main() {
    val outFolder = File("pdf/out")
    val inFolder = File("pdf/in")
    val passFolder = File("pdf/pw")
    if(!outFolder.exists()) outFolder.mkdirs()
    if(!inFolder.exists()) inFolder.mkdirs()
    if(!passFolder.exists()) passFolder.mkdirs()
    if(!File("${passFolder.path}/pass.txt").exists()) throw java.lang.RuntimeException("pw/pass.txt 파일이없습니다..")
    val pwModelList = File("${passFolder.path}/pass.txt").readLines().map {
        val splitString = it.split(",")
        PWModel(name = splitString[0], pw = splitString[1])
    }

    inFolder.listFiles()?.filter { it.isFile && it.name.endsWith(".pdf") }?.forEach { target ->
        pwModelList.forEach { pwModel ->
            if (target.name.startsWith(pwModel.name)) {
                val pdd = PDDocument.load(target)
                val ap = AccessPermission()
                StandardProtectionPolicy(pwModel.pw, pwModel.pw, ap).let { policy ->
                    policy.encryptionKeyLength = 128
                    policy.permissions = ap
                    pdd.protect(policy)
                    pdd.save(outFolder.path + "/${target.name}")
                    pdd.close()
                }
            }
        }
    }

}