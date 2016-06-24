import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;

import de.charite.compbio.jannovar.data.JannovarData;
import de.charite.compbio.jannovar.data.JannovarDataSerializer;
import de.charite.compbio.jannovar.data.SerializationException;
import de.charite.compbio.jannovar.reference.ProjectionException;
import de.charite.compbio.jannovar.reference.TranscriptModel;

public class HGV2Genome {
	static JannovarData data;
	static java.util.List<HGVS> regexlist=new java.util.ArrayList<HGVS>();
    public static void main(String[] args) throws ProjectionException, IOException, SerializationException{
    		CommandLineValues bean = new CommandLineValues(args);		   
	   		data=new JannovarDataSerializer(bean.refseq).load();
			run(bean.input,bean.output,bean.column);
			
					
}
   private static boolean run(String hgvs_file,String vcf_file,int column) throws IOException{
	    Utils.writeHeader(vcf_file,false);
		StringBuffer strbuffer=new StringBuffer();
		FileInputStream fstream = new FileInputStream(hgvs_file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		int count=0;
		while ((strLine = br.readLine()) != null)   {
			String[] strs=strLine.split("\t");
			String code;
			if(strs.length<column){
				System.err.println(hgvs_file+" does not contain "+column+" fields.");
				br.close();
				return false;
			}
			code=strs[column -1];
			HGVS hgvs=new HGVS(code);
			HGVS_Parser parser=new HGVS_Parser(data);
			if(parser.parse(hgvs)){
			if(parser.failedlist.size()>0){
				regexlist.addAll(parser.failedlist);
			}
			strbuffer.append(parser.chr+"\t"+parser.position+"\t"+".\t"+parser.ref+"\t"+parser.alt+"\t100\tPASS\tHGVS="+code+"\n");
			if(count==10000){
				Utils.write(vcf_file, strbuffer.toString(),true);
				strbuffer.setLength(0);
				count=0;
			}
			count++;
			}
		}
		br.close();
		if(count>0){
			Utils.write(vcf_file, strbuffer.toString(),true);
			strbuffer.setLength(0);
			count=0;
		}
		if(regexlist.size()>0){
			Utils.write(vcf_file, HGVS_regex(regexlist,data.getTmByAccession()).toString(),true);
		}
		return true;
   }
   
   private static StringBuffer HGVS_regex(List<HGVS> regexlist2, ImmutableMap<String, TranscriptModel> immutableMap) {
	   ImmutableSet<String> keys=immutableMap.keySet();
	   UnmodifiableIterator<String> refseq_it=keys.iterator();
	   StringBuffer strbuff=new StringBuffer();
	   int len=0;
	   while(refseq_it.hasNext()){
		   java.util.Iterator<HGVS> it=regexlist.iterator();
		   String key_ref=null;
		   key_ref=refseq_it.next();
			while(it.hasNext()){
				HGVS fail_hgvs=it.next();
				if(key_ref!=null&&(key_ref.startsWith(fail_hgvs.getTranscriptCode()))){
					fail_hgvs.setTranscript(key_ref);
					len++;
					strbuff.append(new HGVS_Parser(data).parse(fail_hgvs));
					if(len==regexlist.size()){return strbuff;}
				}				
			}
	     }
	   return strbuff;
       }


}