package school.sptech;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class ConexaoAws {

        public XSSFWorkbook conexaoS3() {


            // 1. Defina a Região do seu bucket
            Region region = Region.US_EAST_1;

            // 2. Crie o cliente S3
            S3Client s3Client = S3Client.builder().region(region).build();

            // 3. Nome do Bucket
            String bucketName = "s3-educadata-dados";

            // 4. A "Key" (nome) do arquivo no S3
            String objectKey = "Book 6.xlsx";

            System.out.println("Tentando ler o arquivo '" + objectKey + "' do S3...");
            XSSFWorkbook workbook = null;

            try (
                    // Passo 1: Baixar o objeto como um InputStream
                    ResponseInputStream<GetObjectResponse> s3ObjectStream = s3Client.getObject(
                            GetObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(objectKey)
                                    .build()
                    )

            ) {
                // SUCESSO!
                System.out.println("Arquivo lido com sucesso na memória!");
                // Recurso 2: O workbook do POI
                workbook = new XSSFWorkbook(s3ObjectStream);




            } catch (S3Exception e) {
                System.err.println("Erro ao baixar o arquivo do S3: " + e.awsErrorDetails().errorMessage());
                e.printStackTrace();
            } catch (IOException e) {
                // Este erro é do Apache POI (ex: arquivo corrompido ou não é .xlsx)
                System.err.println("Erro ao ler o arquivo Excel (POI): " + e.getMessage());
                e.printStackTrace();
            }

            return workbook;

        }
    }




