import { useState } from "react";

interface CartItem {
  name: string;
  price: number;
  taxType: "FOOD" | "GENERAL";
}

function App() {
  const [cart, setCart] = useState<CartItem[]>([
    { name: "サンプル商品A", price: 1000, taxType: "GENERAL" },
    { name: "サンプル食料品B", price: 500, taxType: "FOOD" },
  ]);

  const totalAmount = cart.reduce(
    (sum, item) =>
      sum +
      (item.taxType === "FOOD" ? item.price : Math.floor(item.price * 1.1)),
    0,
  );

  const handleCheckout = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/pos/checkout", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(cart),
      });
      const data = await response.json();
      alert(`決済完了！ 合計: ¥${data.totalAmount}`);
    } catch (error) {
      alert(
        "サーバーに接続できません。Spring Bootが起動しているか確認してください。",
      );
    }
  };

  const handleCancel = () => {
    if (window.confirm("カートの内容をすべてクリアしますか？")) {
      setCart([]);
    }
  };

  return (
    <div className="flex h-screen bg-slate-100 text-slate-900 p-4 gap-4 overflow-hidden">
      {/* 左：注文明細（Cart View） */}
      <section className="flex-1 bg-white rounded-3xl shadow-sm flex flex-col">
        <div className="p-6 border-b">
          <h2 className="text-xl font-black">注文明細</h2>
        </div>
        <div className="flex-1 overflow-y-auto p-4 space-y-3">
          {cart.map((item, index) => (
            <div
              key={index}
              className="flex justify-between items-center p-4 bg-slate-50 rounded-2xl"
            >
              <div>
                <p className="font-bold">{item.name}</p>
                <p className="text-sm text-slate-500">
                  単価: ¥{item.price.toLocaleString()}
                </p>
              </div>
              <p className="font-mono font-bold text-lg">
                ¥
                {(item.taxType === "FOOD"
                  ? item.price
                  : Math.floor(item.price * 1.1)
                ).toLocaleString()}
              </p>
            </div>
          ))}
        </div>
      </section>

      {/* 中：操作パネル（Interaction View） */}
      <section className="w-1/3 flex flex-col gap-4">
        <div className="grid grid-cols-3 gap-2 flex-1">
          {[1, 2, 3, 4, 5, 6, 7, 8, 9, 0, "C", "Del"].map((key) => (
            <button
              key={key}
              className="bg-white hover:bg-slate-50 active:scale-95 transition-all text-2xl font-bold rounded-2xl shadow-sm border border-slate-100"
            >
              {key}
            </button>
          ))}
        </div>
        <div className="grid grid-cols-2 gap-2">
          <button className="bg-blue-600 text-white font-bold py-4 rounded-2xl shadow-lg">
            割引
          </button>
          <button
            onClick={handleCancel}
            className="bg-rose-600 text-white font-bold py-4 rounded-2xl shadow-lg"
          >
            キャンセル
          </button>
        </div>
      </section>

      {/* 右：サマリー（Action View） */}
      <section className="w-1/4 flex flex-col gap-4">
        <div className="bg-slate-900 text-white p-6 rounded-3xl shadow-xl flex flex-col justify-between h-64">
          <p className="text-sm font-bold text-slate-400 text-right uppercase tracking-widest">
            Grand Total
          </p>
          <div className="text-right">
            <span className="text-2xl mr-2">¥</span>
            <span className="text-6xl font-black font-mono tracking-tighter">
              {totalAmount.toLocaleString()}
            </span>
          </div>
        </div>

        <button
          onClick={handleCheckout}
          className="flex-1 bg-emerald-500 hover:bg-emerald-600 text-white rounded-3xl shadow-lg flex items-center justify-center group active:scale-95 transition-all"
        >
          <span className="text-4xl font-black tracking-widest">会計</span>
        </button>
      </section>
    </div>
  );
}

export default App;
