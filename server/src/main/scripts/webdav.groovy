import org.apache.commons.httpclient.*
import org.apache.commons.httpclient.methods.*
import org.apache.commons.httpclient.auth.*
import org.yestech.publish.publisher.webdav.MkColMethod

def f = args[0]
def file = new File(f)

def client = new HttpClient()
def m = new PutMethod('http://webdav.bitgravity.com/test/'+file.name)
//def m = new MkColMethod('http://webdav.bitgravity.com/test')
def up = new UsernamePasswordCredentials('aj@blackboxrepublic.com', '99botb33r')
client.state.setCredentials(AuthScope.ANY, up)



def re = new InputStreamRequestEntity(new BufferedInputStream(new FileInputStream(file)));
m.requestEntity = re;

client.executeMethod(m)

println "push : ${m.statusCode} : ${m.statusText}"


