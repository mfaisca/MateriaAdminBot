package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Fang extends _Default{
	public Fang() {
		super("Fang", "oerba yun fang");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{2950};
				break;
			case HP:
				ids = new int[]{5102, 5103};
				break;
			case S1:
				ids = new int[]{5095};
				break;
			case S2:
				ids = new int[]{5098};
				break;
			case EX:
				ids = new int[]{5111, 5110};
				break;
			case AA:
				ids = new int[]{5115};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}