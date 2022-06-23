import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        //TODO: write code here
        int length = inputs.size();
        String result = "============= Order details =============\n";
        String salesName = "";
        List<String> salesList = new ArrayList<>();
        double originTotal = 0;
        double salesTotal = 0;
        for(int i =0;i<length;i++){
            String id = inputs.get(i).split(" ")[0];
            int count = Integer.parseInt(inputs.get(i).split(" ")[2]);
            for (Item item : itemRepository.findAll()) {
                if (item.getId().equals(id)) {
                    double singleTotal = item.getPrice()*count;
                    result = result.concat(MessageFormat.format("{0} x {1} = {2} yuan\n", item.getName(), count,singleTotal));
                    for(SalesPromotion salesPromotion : salesPromotionRepository.findAll()){
                        for(String salesid : salesPromotion.getRelatedItems()){
                            if(salesid.equals(id)){
                                salesTotal += singleTotal/2;
                                salesList.add(item.getName());
                            }
                        }
                    }

                    originTotal +=item.getPrice()*count;
                    break;
                }
            }
        }
        salesName = salesName.join("��",salesList);
        String deductResult = result.concat("-----------------------------------\n"+"Promotion used:\n" +
                "��30��6 yuan��saving 6 yuan\n" +"-----------------------------------\n"
                +MessageFormat.format("Total��{0} yuan\n",originTotal-6) + "===================================");
        String salesResult =result.concat("-----------------------------------\n"+"Promotion used:\n"
                +MessageFormat.format("Half price for certain dishes '('{0}')'��saving {1} yuan\n",salesName,salesTotal)
                +"-----------------------------------\n"
                +MessageFormat.format("Total��{0} yuan\n",originTotal-salesTotal) + "===================================");
        if(originTotal >=30){
            if(originTotal-6 <= originTotal-salesTotal){
                result = deductResult;
            }else {
                result = salesResult;
            }
        }else if(salesTotal != 0){
            result = salesResult;
        }else{
            result = result.concat("-----------------------------------\n"
                    +MessageFormat.format("Total��{0} yuan\n",originTotal) + "===================================");
        }

        return result;
    }
}
