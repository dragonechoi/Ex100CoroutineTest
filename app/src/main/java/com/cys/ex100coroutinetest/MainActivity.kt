package com.cys.ex100coroutinetest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.ListFormatter.Width
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.cys.ex100coroutinetest.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Coroutine[코루틴] - 경량 스레드 : 스레드를 멈추지 않고 비동기 처리 - 하나의 스레드 안에 여러개의 코루틴 실행
        //스레드가 요리사라면 멀티스레드는 여러 요리사가 화구(CPU)를 번갈아 사용하는 기술, 다른 요리사가 사용 하는 중에는 기존 요리사는 동작을 멈춤
        //코루틴은 하나의 요리사(스레드) 파스타를 만들면서 스테이크 까지 함께 굽는 형식을 설명. 즉, 펜이 2개라 비유 가능, 자리를 비켜가면 멈추는 행동이 없어서 속도가 조금 더 빠르게 동시 작업이 가능

        //코루틴을 구동하는 2개의 범위(Scope)가 존재
        // 1.GlobalScope     : 앱 전체의 생명주기와 함께관리 되는 범위
        // 2.CoroutineScope  : 버튼 클릭 같은 특정 이벤트 순간에 해야할 Job을 위해 실행 되는 범위 [ ex . network통신 , DB CRUD, 특정 연산수행 등]

        // 실습 1) GlobalScope 코드 연습
        binding.btn.setOnClickListener {
              //코루틴 없이 오래걸리는 작업 실행
//            for (n in 0..9){
//                Log.d("TAG","n:${n}")
//                Thread.sleep(500)
//            }

            //비동기 작업으로 위 작업을 수행 - 코루틴 사용해보기
            GlobalScope.launch {
                for (n in 0..9) {
                    Log.d("TAG", "n:$n - ${Thread.currentThread().name}")
                    delay(500)
                }
            }

            Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show()

        }

        //CoroutineScope 비동기 실행
        //CoroutineScope는 GlobalScope와 다르게 해당 작업을 어떤 스레드에게 보낼지 결정하는 Dispatcher[디스패처]를 지정해야 함.
        //Dispatcher의 종류
        //1] Dispatchers.Default - 기본 스레드풀의 스레드를 사용 [ CPU 작업이 많은 연산 작업에 적합]
        //2) Dispatchers.IO      - DB나 네트워크 IO 스레드를 사용 [파일 입출력 , 서버 작업에 적합]
        //3) Dispatchers.Main    -Main 스레드를 사용 [UI 작업 등에 적합]
        //4) Dispatchers.Unconfined - 조금 특별한 디스패처 [해당 코루틴을 호출하는 스레드에서 실행]
        binding.btn2.setOnClickListener {
            //Dispatchers.Default
            CoroutineScope(Dispatchers.Default).launch {
                for (n in 100..110){
                    Log.d("TAG","n: $n - ${Thread.currentThread().name}")

                   // binding.tv.text="n:${n}" //UI 변경 불가능

                    Thread.sleep(500)
                }
            }
            Toast.makeText(this, "bbb", Toast.LENGTH_SHORT).show()
        }
        binding.btn3.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                for(n in 200..210){
                    binding.tv.text="n:${n}-${Thread.currentThread().name}" //UI 작업 가능
                    delay(500)
                }
            }
            Toast.makeText(this, "ccc", Toast.LENGTH_SHORT).show()
        }
        binding.btn4.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                //네트워크 이미지 불러오기 - 에러 MainThread는 네트워크 작업불가능

                val url=URL("https://media.istockphoto.com/id/1425207162/ko/%EC%82%AC%EC%A7%84/%EC%9B%A8%EC%9D%BC%EC%8A%A4-%EC%96%B4-%EC%BD%94%EA%B8%B0-%ED%8E%A8%EB%B8%8C%EB%A1%9C%ED%81%AC%EC%9D%98-%EC%8A%A4%ED%8A%9C%EB%94%94%EC%98%A4-%EC%83%B7%EC%9D%B4-%EB%8C%80%EC%A0%91%EC%9D%84-%EC%9E%A1%EC%95%98%EC%8A%B5%EB%8B%88%EB%8B%A4.jpg?s=2048x2048&w=is&k=20&c=zn8d4-FvrNqhYz1-QRYm4G7P-fGT2-igOy6PpHDU894=")
                val bm:Bitmap= BitmapFactory.decodeStream(url.openStream())

                binding.iv.setImageBitmap(bm)
            }
        }
        binding.btn5.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                val url=URL("https://media.istockphoto.com/id/1425207162/ko/%EC%82%AC%EC%A7%84/%EC%9B%A8%EC%9D%BC%EC%8A%A4-%EC%96%B4-%EC%BD%94%EA%B8%B0-%ED%8E%A8%EB%B8%8C%EB%A1%9C%ED%81%AC%EC%9D%98-%EC%8A%A4%ED%8A%9C%EB%94%94%EC%98%A4-%EC%83%B7%EC%9D%B4-%EB%8C%80%EC%A0%91%EC%9D%84-%EC%9E%A1%EC%95%98%EC%8A%B5%EB%8B%88%EB%8B%A4.jpg?s=2048x2048&w=is&k=20&c=zn8d4-FvrNqhYz1-QRYm4G7P-fGT2-igOy6PpHDU894=")
                val bm:Bitmap= BitmapFactory.decodeStream(url.openStream())

                //binding.iv.setImageBitmap(bm)//UI 변경 불가능
                withContext(Dispatchers.Main){
                    binding.iv.setImageBitmap(bm)
                }

            }
        }

    }
}