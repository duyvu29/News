package com.example.app_readnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayList_titile, arraylink;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ánh xạ
        listView=(ListView) findViewById(R.id.listview);
        // code
        arrayList_titile = new ArrayList<>();
        arraylink=new ArrayList<>();
        adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList_titile);
        listView.setAdapter(adapter);

        //new ReadRss().execute("https://vnexpress.net/rss/so-hoa.rss");
        new ReadRss(this).execute("https://vnexpress.net/rss/cuoi.rss");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("linknews", arraylink.get(position));
                startActivity(intent);
                // Toast.makeText(MainActivity.this, arraylink.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private class ReadRss extends AsyncTask<String, Void,String>{
        private Context context;

        public ReadRss(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder conten= new StringBuilder();
            try {
                URL url= new URL(strings[0]);

                InputStreamReader inputStreamReader= new InputStreamReader(url.openConnection().getInputStream());

                BufferedReader bufferedReader= new BufferedReader(inputStreamReader);

                String line="";
                while ((line= bufferedReader.readLine()) != null){
                    conten.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return conten.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMparser parser= new XMLDOMparser();

            Document document=parser.getDocument(s);

            NodeList nodeList=document.getElementsByTagName("item");
            String tieude="";
            Log.d("MainActivity", String.valueOf(nodeList.getLength()));

            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    Element elm = (Element) nodeList.item(i);
                    String title_value = getNodeValue("title", elm);
                    String pubDate_value = getNodeValue("pubDate", elm);
                    Log.d("Title", title_value);
                    Log.d("pubDate", pubDate_value);
                }
                Element element= (Element) nodeList.item(i);
                tieude= parser.getValue(element,"title");
                arrayList_titile.add(tieude);
                arraylink.add(parser.getValue(element,"link"));
            }
            adapter.notifyDataSetChanged();

        }

        private String getNodeValue(String tag, Element element) {
            NodeList nodeList = element.getElementsByTagName(tag);
            Node node = nodeList.item(0);
            if (node != null) {
                if (node.hasChildNodes()) {
                    Node child = node.getFirstChild();
                    while (child != null) {
                        if (child.getNodeType() == Node.TEXT_NODE) {
                            return child.getNodeValue();
                        }
                    }
                }
            }
            return "";
        }
    }

    }
